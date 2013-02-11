/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.lpbport.lpb;

import com.sun.xml.internal.ws.api.pipe.NextAction;
import java.util.Set;
import se.kth.ict.id2203.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.flp2p.Flp2pDeliver;
import se.kth.ict.id2203.lpbport.PbBroadcast;
import se.kth.ict.id2203.lpbport.ProbabilisticBroadcast;
import se.kth.ict.id2203.pfdp.pfd.CheckTimeOut;
import se.kth.ict.id2203.unbport.UnDeliver;
import se.kth.ict.id2203.unbport.UnreliableBroadcast;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
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

    public LazyPb() {
        /*subscribe(eachHandler, respective port);*/
        subscribe(handleInit, control);
        subscribe(handlePbBroadcast, pb);
        subscribe(handleUnDeliver, ub);
        subscribe(handleFllRequestDeliver, flp2p);
        subscribe(handleFllDataDeliver, flp2p);
        subscribe(handleCheckTimeOut, timer);

    }
    private int[] next = null;
    private int lsn = 0;
    private Set<Address> pending = null;
    private Set<Address> stored = null;
    
    
    /*Handlers*/
    Handler<LazyPbInit> handleInit = new Handler<LazyPbInit>() {
        @Override
        public void handle(LazyPbInit e) {
            //TODO
        }
    };
    Handler<PbBroadcast> handlePbBroadcast = new Handler<PbBroadcast>() {
        @Override
        public void handle(PbBroadcast e) {
            //TODO
        }
    };
    Handler<UnDeliver> handleUnDeliver = new Handler<UnDeliver>() {
        @Override
        public void handle(UnDeliver e) {
            //TODO
        }
    };
    Handler<Flp2pDeliver> handleFllRequestDeliver = new Handler<Flp2pDeliver>() {
        @Override
        public void handle(Flp2pDeliver e) {
            //TODO
        }
    };
    Handler<Flp2pDeliver> handleFllDataDeliver = new Handler<Flp2pDeliver>() {
        @Override
        public void handle(Flp2pDeliver e) {
            //TODO
        }
    };
    Handler<CheckTimeOut> handleCheckTimeOut = new Handler<CheckTimeOut>() {
        @Override
        public void handle(CheckTimeOut e) {
            //TODO
        }
    };
}
