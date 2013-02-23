/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.eldport.eld;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import se.kth.ict.id2203.eldport.EventualLeaderDetection;
import se.kth.ict.id2203.eldport.Trust;
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
public class ELD extends ComponentDefinition {

    Negative<EventualLeaderDetection> eld = provides(EventualLeaderDetection.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    Positive<Timer> timer = requires(Timer.class);

    public ELD() {
        /*subscribe(eachHandler, respective port);*/
        subscribe(handleInit, control);
        subscribe(handleTimeOut, timer);
        subscribe(handleHeartBeat, pp2p);
    }
    private Set<Address> allNodes;
    private Address leader;
    private Address self;
    private Set<Address> candidateSet;
    private int period;
    private int delta;

    private Address electLeader(Set<Address> addressSet) {
        Address toReturn = new Address(null, 42, 32000000);
        Iterator<Address> iter = addressSet.iterator();
        while (iter.hasNext()) {
            Address address = iter.next();
            if (address.getId() < toReturn.getId()) {
                toReturn = address;
            }
        }
        return toReturn;
    }

    private void sendHeartBeat(Set<Address> nodes) {
        Iterator<Address> iter = nodes.iterator();
        while (iter.hasNext()) {
            Address address = iter.next();
            trigger(new Pp2pSend(address, new EldHeartBeat(self)), pp2p);
        }
    }

    private void startTimer(int delay) {
        ScheduleTimeout st;
        st = new ScheduleTimeout(delay);
        st.setTimeoutEvent(new EldTimeOut(st));
        trigger(st, this.timer);
    }
    Handler<ELDInit> handleInit = new Handler<ELDInit>() {
        @Override
        public void handle(ELDInit e) {
            allNodes = new HashSet<Address>(e.getTopology().getAllAddresses());
            self = e.getTopology().getSelfAddress();
            delta = e.getDelta();
            leader = electLeader(allNodes);
            trigger(new Trust(leader), eld);

            sendHeartBeat(allNodes);
            candidateSet = new HashSet<Address>();
            candidateSet.clear();
            startTimer(period);
        }
    };
    Handler<EldTimeOut> handleTimeOut = new Handler<EldTimeOut>() {
        @Override
        public void handle(EldTimeOut e) {
            Address newLeader = electLeader(candidateSet);
            if (leader != newLeader && newLeader.getIp() != null) {
                period = period + delta;
                leader = newLeader;
                trigger(new Trust(leader), eld);
            }
            sendHeartBeat(allNodes);
            candidateSet.clear();
            startTimer(period);
        }
    };
    Handler<EldHeartBeat> handleHeartBeat = new Handler<EldHeartBeat>() {
        @Override
        public void handle(EldHeartBeat e) {
            candidateSet.add(e.getSource());
        }
    };
}
