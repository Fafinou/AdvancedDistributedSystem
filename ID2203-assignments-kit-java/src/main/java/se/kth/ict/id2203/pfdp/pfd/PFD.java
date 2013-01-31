/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.pfdp.pfd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import se.kth.ict.id2203.pfdp.Crash;
import se.kth.ict.id2203.pfdp.PerfectFailureDetector;
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
public class PFD extends ComponentDefinition {

    Negative<PerfectFailureDetector> pfd = provides(PerfectFailureDetector.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    Positive<Timer> timer = requires(Timer.class);

    public PFD() {
        /*subscribe(eachHandler, respective port);*/
        subscribe(handleInit, control);
        subscribe(handleHeartBeatTimeOut, timer);
        subscribe(handleHeartBeatMessage, pp2p);
        subscribe(handleCheckTimeOut, timer);
    }
    
    private Address self;
    private Set<Address> allNodes = null;
    private ArrayList<Address> alive = null;
    private ArrayList<Address> detected = null;
    private int gamma = 0;
    private int delta = 0;

    
    
    private void startTimer(int time) {
        ScheduleTimeout st = new ScheduleTimeout(time);
        st.setTimeoutEvent(new HeartBeatTimeOut(st));
        trigger(st, this.timer);
    }
    
    
    /*Handlers*/
    Handler<PfdInit> handleInit = new Handler<PfdInit>() {
        @Override
        public void handle(PfdInit event) {
            /*may be buggy*/
            allNodes = event.getTopology().getAllAddresses();
            alive = new ArrayList<Address>(allNodes);
            detected = new ArrayList<Address>();
            gamma = event.getGamma();
            delta = event.getDelta();
            self = event.getSelf();

            /*starting the HeartBeat time out*/

            startTimer(gamma);

            /*starting the Check time out */

            startTimer(gamma + delta);

        }
    };
    Handler<HeartBeatTimeOut> handleHeartBeatTimeOut = new Handler<HeartBeatTimeOut>() {
        @Override
        public void handle(HeartBeatTimeOut e) {

            /*iterate over all correct adresses and send an Heartbeat*/
            for (Iterator<Address> it = alive.iterator(); it.hasNext();) {
                Address address = it.next();
                trigger(new Pp2pSend(address, new HeartBeatMessage(self)), pp2p);
            }
            
            startTimer(gamma);
        }
    };
    
    
    Handler<HeartBeatMessage> handleHeartBeatMessage = new Handler<HeartBeatMessage>() {
        @Override
        public void handle(HeartBeatMessage e) {
            alive.add(e.getSource());
        }
    };
    
    
    Handler<CheckTimeOut> handleCheckTimeOut = new Handler<CheckTimeOut>() {
        @Override
        public void handle(CheckTimeOut e) {
                        Iterator<Address> iterNodes = allNodes.iterator();
            while (iterNodes.hasNext()) {
                Address address = iterNodes.next();
                if((!alive.contains(address)) && (!detected.contains(address))){
                    detected.add(address);
                    trigger(new Crash(address), pfd);
                }
            }
            alive.clear();
            startTimer(gamma + delta);
        }
    };
    
}
