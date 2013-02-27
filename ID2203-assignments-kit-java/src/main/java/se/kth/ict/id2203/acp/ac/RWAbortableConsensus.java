package se.kth.ict.id2203.acp.ac;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.acp.AbortableConsensus;
import se.kth.ict.id2203.acp.AcDecide;
import se.kth.ict.id2203.acp.AcPropose;
import se.kth.ict.id2203.application.Application4;
import se.kth.ict.id2203.bebport.BEB;
import se.kth.ict.id2203.bebport.BebBroadcast;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.Pp2pSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class RWAbortableConsensus extends ComponentDefinition {

    Negative<AbortableConsensus> ac = provides(AbortableConsensus.class);
    Positive<BEB> beb = requires(BEB.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application4.class);

    public RWAbortableConsensus() {
        subscribe(handleInit, control);
        subscribe(handleNack, pp2p);
        subscribe(handlePropose, ac);
        subscribe(handleRack, pp2p);
        subscribe(handleWack, pp2p);
        subscribe(handleRead, beb);
        subscribe(handleWrite, beb);
    }
    private Set<Integer> seenId;
    private int N;
    private int majority;
    private Set<Address> allNodes;
    private Address self;
    private int maxInstance;
    private Integer[] tempValue;
    private Integer[] val;
    private Integer[] wAcks;
    private Integer[] rts;
    private Integer[] wts;
    private Integer[] tstamp;
    private Set<AcValue>[] readSet;

    private void initInstance(int id) {
        if (!seenId.contains(id)) {

            tempValue[id] = null;

            val[id] = null;


            wAcks[id] = 0;
            rts[id] = 0;
            wts[id] = 0;


            tstamp[id] = self.getId();


            readSet[id] = new HashSet<AcValue>();
            readSet[id].clear();

            seenId.add(id);

        }
    }

    private AcValue highest(Set<AcValue> set) {
        AcValue toReturn = new AcValue(-1, null);
        Iterator<AcValue> iter = set.iterator();
        while (iter.hasNext()) {
            AcValue val1 = iter.next();
            if (val1.ts > toReturn.ts) {
                toReturn = val1;
            }
        }
        return toReturn;
    }
    Handler<RWACInit> handleInit = new Handler<RWACInit>() {
        @Override
        public void handle(RWACInit e) {
            seenId = new HashSet<Integer>();
            seenId.clear();
            allNodes = e.getTopology().getAllAddresses();
            self = e.getTopology().getSelfAddress();
            N = allNodes.size();
            majority = (N / 2) + 1;
            maxInstance = e.getMaxInstance();
            readSet = new Set[maxInstance];
            tstamp = new Integer[maxInstance];
            tempValue = new Integer[maxInstance];
            val = new Integer[maxInstance];
            wAcks = new Integer[maxInstance];
            rts = new Integer[maxInstance];
            wts = new Integer[maxInstance];
        }
    };
    Handler<AcPropose> handlePropose = new Handler<AcPropose>() {
        @Override
        public void handle(AcPropose e) {
            int id = e.getId();
            initInstance(id);
            tstamp[id] = tstamp[id] + N;
            tempValue[id] = e.getVal();
            trigger(new BebBroadcast(new Read(self, id, tstamp[id])), beb);
        }
    };
    Handler<Read> handleRead = new Handler<Read>() {
        @Override
        public void handle(Read e) {
            int id = e.getId();
            int ts = e.getTstamp();
            initInstance(id);
            if (rts[id] >= ts || wts[id] >= ts) {
                trigger(new Pp2pSend(e.getSource(), new NAck(self, id)), pp2p);
            } else {
                rts[id] = ts;
                trigger(new Pp2pSend(e.getSource(), new RAck(self, id, wts[id], val[id], ts)), pp2p);
            }
        }
    };
    Handler<NAck> handleNack = new Handler<NAck>() {
        @Override
        public void handle(NAck e) {
            int id = e.getId();
            readSet[id].clear();
            wAcks[id] = 0;
            trigger(new AcDecide(id, null), ac);
        }
    };
    Handler<RAck> handleRack = new Handler<RAck>() {
        @Override
        public void handle(RAck e) {
            int id = e.getId();
            int ts = e.getWts();
            Integer v = e.getVal();
            int sentts = e.getTs();

            if (sentts == tstamp[id]) {

                readSet[id].add(new AcValue(ts, v));
                if (readSet[id].size() == majority) {

                    AcValue acVal = highest(readSet[id]);
                    if (!(acVal.val == null)) {
                        tempValue[id] = acVal.val;
                    }
                    trigger(new BebBroadcast(new Write(self, id, tstamp[id], tempValue[id])), beb);
                }
            }
        }
    };
    Handler<Write> handleWrite = new Handler<Write>() {
        @Override
        public void handle(Write e) {
            int id = e.getId();
            int ts = e.getTs();
            int v = e.getVal();
            initInstance(id);

            if (rts[id] > ts || wts[id] > ts) {
                trigger(new Pp2pSend(e.getSource(), new NAck(self, id)), pp2p);
            } else {
                val[id] = v;
                wts[id] = ts;
                trigger(new Pp2pSend(e.getSource(), new WAck(self, id, ts)), pp2p);
            }
        }
    };
    Handler<WAck> handleWack = new Handler<WAck>() {
        @Override
        public void handle(WAck e) {
            int id = e.getId();
            int sentts = e.getTs();
            if (sentts == tstamp[id]) {
                wAcks[id] = wAcks[id] + 1;
                if (wAcks[id] == majority) {
                    readSet[id].clear();
                    wAcks[id] = 0;
                    trigger(new AcDecide(id, tempValue[id]), ac);
                }
            }
        }
    };
}
