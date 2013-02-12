/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.lpbport.lpb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.application.Application2;
import se.kth.ict.id2203.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.flp2p.Flp2pSend;
import se.kth.ict.id2203.lpbport.PbBroadcast;
import se.kth.ict.id2203.lpbport.PbDeliver;
import se.kth.ict.id2203.lpbport.ProbabilisticBroadcast;
import se.kth.ict.id2203.unbport.UnBroadcast;
import se.kth.ict.id2203.unbport.UnDeliver;
import se.kth.ict.id2203.unbport.UnreliableBroadcast;
import se.kth.ict.id2203.unbport.unb.DataMessage;
import se.kth.ict.id2203.unbport.unb.RequestMessage;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author fingolfin
 */
public class LazyPb extends ComponentDefinition {

    Negative<ProbabilisticBroadcast> pb = provides(ProbabilisticBroadcast.class);
    Positive<UnreliableBroadcast> ub = requires(UnreliableBroadcast.class);
    Positive<FairLossPointToPointLink> flp2p = requires(FairLossPointToPointLink.class);
    Positive<Timer> timer = requires(Timer.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application2.class);

    public LazyPb() {
        /*subscribe(eachHandler, respective port);*/
        subscribe(handleInit, control);
        subscribe(handlePbBroadcast, pb);
        subscribe(handleUnDeliver, ub);
        subscribe(handleFllRequestDeliver, flp2p);
        subscribe(handleFllDataDeliver, flp2p);
        subscribe(handleLpbTimeOut, timer);
        subscribe(handleCheckPendingTimeOut, timer);

    }

    private HashMap<Address, Integer> initNext(Set<Address> allAdd) {
        HashMap<Address, Integer> toReturn = new HashMap<Address, Integer>();
        Iterator<Address> iter = allAdd.iterator();
        while (iter.hasNext()) {
            Address address = iter.next();
            toReturn.put(address, 1);
        }
        return toReturn;
    }
    private HashMap<Address, Integer> next = null;
    private int lsn = 0;
    private Address self;
    private ArrayList<DataMessage> pending = null;
    private ArrayList<DataMessage> stored = null;
    private ArrayList<Address> allNodes;
    private int fanOut = 0;
    private double storeThreshold = 0;
    private int maxRound = 0;
    private int delay;

    private void gossip(RequestMessage msg) {
        Collections.shuffle(allNodes);
        Iterator<Address> iter = allNodes.iterator();
        int i = 0;
        while (iter.hasNext() && i < fanOut) {
            i++;
            trigger(new Flp2pSend(iter.next(), msg), pb);
        }
    }

    private void startTimer(int delay, Address source, int sequenceNumber) {
        ScheduleTimeout st;
        st = new ScheduleTimeout(delay);
        st.setTimeoutEvent(new LpbTimeOut(st, source, sequenceNumber));
        trigger(st, this.timer);
    }

    private boolean existMessageSequenceNumber(ArrayList<DataMessage> pending, int sequenceNumber) {
                Iterator<DataMessage> iter = pending.iterator();
        while (iter.hasNext()) {
            DataMessage dataMessage = iter.next();
            if (dataMessage.getSequenceNumber() == sequenceNumber) {
                return true;
            }
        }
        return false;
    }
    
    private DataMessage getMessageToDeliver(ArrayList<DataMessage> pending){
        Iterator<DataMessage> iter = pending.iterator();
        while (iter.hasNext()) {
            DataMessage dataMessage = iter.next();
            if (dataMessage.getSequenceNumber() == next.get(dataMessage.getSource())) {
                    return dataMessage;
            }
        }
        return null;
    }
    

    private DataMessage searchMessage(ArrayList<DataMessage> stored,
            Address source, int seqNbr) {
        Iterator<DataMessage> iter = stored.iterator();
        while (iter.hasNext()) {
            DataMessage dataMessage = iter.next();
            if (dataMessage.getSequenceNumber() == seqNbr
                    && dataMessage.getSource() == source) {
                return dataMessage;
            }
        }
        return null;
    }
    /*Handlers*/
    Handler<LazyPbInit> handleInit = new Handler<LazyPbInit>() {
        @Override
        public void handle(LazyPbInit e) {
            logger.info("start");
            next = initNext(e.getTopology().getAllAddresses());
            lsn = 0;
            pending = new ArrayList<DataMessage>();
            stored = new ArrayList<DataMessage>();
            self = e.getTopology().getSelfAddress();
            allNodes = new ArrayList<Address>(e.getTopology().getNeighbors(self));
            fanOut = e.getFanOut();

            /*init storeThreshold*/
            storeThreshold = e.getStoreThreshold();
            maxRound = e.getMaxRound();
            delay = e.getDelay();

            /*Start the time out of pending lists check*/
            ScheduleTimeout st = new ScheduleTimeout(1000);
            st.setTimeoutEvent(new CheckPendingTimeOut(st));
            trigger(st, timer);

        }
    };
    Handler<PbBroadcast> handlePbBroadcast = new Handler<PbBroadcast>() {
        @Override
        public void handle(PbBroadcast e) {
            lsn++;
            trigger(new UnBroadcast(new DataMessage(self,lsn, e.getMsg())), ub);
        }
    };
    Handler<UnDeliver> handleUnDeliver = new Handler<UnDeliver>() {
        @Override
        public void handle(UnDeliver e) {
            DataMessage tmpMsg = e.getMsg();
            if (Math.random() > storeThreshold) {
                stored.add(tmpMsg);
            } else if (tmpMsg.getSequenceNumber()
                    == next.get(tmpMsg.getSource())) {
                logger.info("didn't store message "+tmpMsg.getMsg()+" from "+tmpMsg.getSource());
                int newNext = next.get(tmpMsg.getSource());
                newNext++;
                next.put(tmpMsg.getSource(), newNext);

                /*deliver*/
                trigger(new PbDeliver(tmpMsg.getSource(), tmpMsg.getMsg()), pb);

            } else if (tmpMsg.getSequenceNumber()
                    > next.get(tmpMsg.getSource())) {
                logger.info("didn't store message "+tmpMsg.getMsg()+" from "+tmpMsg.getSource());
                pending.add(tmpMsg);
                for (int i = next.get(tmpMsg.getSource());
                        i <= (tmpMsg.getSequenceNumber() - 1);
                        i++) {
                    if (!existMessageSequenceNumber(pending, i)) {
                        gossip(new RequestMessage(self, tmpMsg.getSource(), i, maxRound - 1));
                    }
                }
                startTimer(delay, tmpMsg.getSource(), tmpMsg.getSequenceNumber());
            }
        }
    };
    Handler<RequestMessage> handleFllRequestDeliver = new Handler<RequestMessage>() {
        @Override
        public void handle(RequestMessage e) {
            DataMessage toReply = searchMessage(stored,
                    e.getRequestedSource(),
                    e.getSequenceNumber());
            if (toReply != null) {
                trigger(new Flp2pSend(e.getSource(), toReply), flp2p);
            } else if (e.getTTL() > 0) {
                gossip(new RequestMessage(e.getSource(), e.getRequestedSource(),
                        e.getSequenceNumber(), e.getTTL() - 1));
            }
        }
    };
    Handler<DataMessage> handleFllDataDeliver = new Handler<DataMessage>() {
        @Override
        public void handle(DataMessage e) {
            pending.add(e);
        }
    };
    Handler<CheckPendingTimeOut> handleCheckPendingTimeOut = new Handler<CheckPendingTimeOut>() {
        @Override
        public void handle(CheckPendingTimeOut e) {
            DataMessage dataMsg = getMessageToDeliver(pending);
            //logger.info("starting check pending");
            while(dataMsg != null){
                next.put(dataMsg.getSource(), next.get(dataMsg.getSource())+1);
                Address source = dataMsg.getSource();
                String msg = dataMsg.getMsg();
                pending.remove(dataMsg);
                logger.info("Recovered msg "+ msg + " from "+ source + " with gossiping");
                trigger(new PbDeliver(source, msg), pb);
                dataMsg = getMessageToDeliver(pending);
            }
            
            /*Start the time out of pending lists check*/
            ScheduleTimeout st = new ScheduleTimeout(1000);
            st.setTimeoutEvent(new CheckPendingTimeOut(st));
            trigger(st, timer);
        }
    };
    Handler<LpbTimeOut> handleLpbTimeOut = new Handler<LpbTimeOut>() {
        @Override
        public void handle(LpbTimeOut e) {
            logger.info("Unable to find a lost message... Skiping");
            if (e.getSequenceNumber() > next.get(e.getSource())) {
                next.put(e.getSource(), e.getSequenceNumber() + 1);
            }
        }
    };
}
