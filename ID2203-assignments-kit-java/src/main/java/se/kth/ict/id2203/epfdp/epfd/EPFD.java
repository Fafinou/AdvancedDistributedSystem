/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.epfdp.epfd;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.application.Application1a;
import se.kth.ict.id2203.epfdp.EventuallyPerfectFailureDetector;
import se.kth.ict.id2203.epfdp.Restore;
import se.kth.ict.id2203.epfdp.Suspect;
import se.kth.ict.id2203.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.flp2p.Flp2pSend;
import se.kth.ict.id2203.pfdp.pfd.CheckTimeOut;
import se.kth.ict.id2203.pfdp.pfd.HeartBeatMessage;
import se.kth.ict.id2203.pfdp.pfd.HeartBeatTimeOut;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.Pp2pSend;
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
public class EPFD extends ComponentDefinition {

    Negative<EventuallyPerfectFailureDetector> epfd = provides(EventuallyPerfectFailureDetector.class);
    Positive<FairLossPointToPointLink> flp2p = requires(FairLossPointToPointLink.class);
    Positive<Timer> timer = requires(Timer.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application1a.class);

    private enum TypeTimeOut {

        HEARTBEAT, CHECK
    }

    public EPFD() {
        /*subscribe(eachHandler, respective port);*/
        subscribe(handleInit, control);
        subscribe(handleHeartBeatTimeOut, timer);
        subscribe(handleHeartBeatMessage, flp2p);
        subscribe(handleCheckTimeOut, timer);
    }
    private Address self;
    private Set<Address> allNodes = null;
    private Set<Address> alive = null;
    private Set<Address> suspected = null;
    private int period = 0;
    private int timeDelay = 0;
    private int periodIncreaser = 0;

    private void startTimer(int time, TypeTimeOut type) {
        //0 = heart beat else check
        ScheduleTimeout st;
        switch (type) {
            case HEARTBEAT:
                st = new ScheduleTimeout(time);
                st.setTimeoutEvent(new HeartBeatTimeOut(st));
                trigger(st, this.timer);
                break;
            case CHECK:
                st = new ScheduleTimeout(time);
                st.setTimeoutEvent(new CheckTimeOut(st));
                trigger(st, this.timer);
                break;
            default:
                logger.info("wrong type of timeout");
                break;
        }
    }
    
    private boolean intersectionIsEmpty(Set<Address> s1, Set<Address> s2){
        Set<Address> intersection = new HashSet<Address>(s1);
        intersection.retainAll(s2);
        return intersection.isEmpty();
    }
    
    /*Handlers*/
    Handler<EPFDinit> handleInit = new Handler<EPFDinit>() {

        @Override
        public void handle(EPFDinit e) {
            logger.info("init of epfd");
            timeDelay = e.getTimeDelay();
            periodIncreaser = e.getPeriodIncreaser();
            period = timeDelay;
            self = e.getTopology().getSelfAddress();
            allNodes = e.getTopology().getNeighbors(self);
            alive = new HashSet<Address>();
            suspected = new HashSet<Address>();
            
            /*start timers*/
            startTimer(timeDelay, TypeTimeOut.HEARTBEAT);
            startTimer(period, TypeTimeOut.CHECK);
            
        }
    };
    Handler<HeartBeatTimeOut> handleHeartBeatTimeOut = new Handler<HeartBeatTimeOut>() {
        @Override
        public void handle(HeartBeatTimeOut e) {
            /*iterate over all correct adresses and send an Heartbeat*/
            for (Iterator<Address> it = allNodes.iterator(); it.hasNext();) {
                
                Address address = it.next();
                //logger.info("send an heartbeat to {}", address);
                trigger(new Flp2pSend(address, new HeartBeatMessagesFl(self)), flp2p);
            }

            startTimer(timeDelay, TypeTimeOut.HEARTBEAT);
        }
    };
    Handler<HeartBeatMessagesFl> handleHeartBeatMessage = new Handler<HeartBeatMessagesFl>() {
        @Override
        public void handle(HeartBeatMessagesFl e) {
            //logger.info("received an answer from {}",e.getSource());
            alive.add(e.getSource());
        }
    };
    
    
    Handler<CheckTimeOut> handleCheckTimeOut = new Handler<CheckTimeOut>() {
        @Override
        public void handle(CheckTimeOut e) {
            if(intersectionIsEmpty(alive, suspected)){
                period = period + periodIncreaser;
            }
            Iterator<Address> iterNodes = allNodes.iterator();
            while (iterNodes.hasNext()) {
                Address address = iterNodes.next();
                if ((!alive.contains(address)) && (!suspected.contains(address))) {
                    suspected.add(address);
                    trigger(new Suspect(address, period), epfd);
                }else if((alive.contains(address)) && (suspected.contains(address))){
                    suspected.remove(address);
                    trigger(new Restore(address, period), epfd);
                }
            }
            alive.clear();
            startTimer(period, TypeTimeOut.CHECK);
        }
    };
}
