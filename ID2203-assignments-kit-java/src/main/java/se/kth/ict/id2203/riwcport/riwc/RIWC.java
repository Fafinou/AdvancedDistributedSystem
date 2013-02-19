package se.kth.ict.id2203.riwcport.riwc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.application.Application3a;
import se.kth.ict.id2203.bebport.BEB;
import se.kth.ict.id2203.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.lpbport.ProbabilisticBroadcast;
import se.kth.ict.id2203.pfdp.PerfectFailureDetector;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.riwcport.AtomicRegister;
import se.kth.ict.id2203.unbport.UnreliableBroadcast;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author ALEX
 */
public class RIWC extends ComponentDefinition{
    Negative<AtomicRegister> ar = provides(AtomicRegister.class);
    Positive<BEB> beb = requires(BEB.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    Positive<PerfectFailureDetector> pfd = requires (PerfectFailureDetector.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application3a.class);

    public RIWC() {
    /*subscribe(eachHandler, respective port);*/
        

    /*Handlers*/
    Handler<RIWCInit> handleInit = new Handler<RIWCInit>() {
        @Override
        public void handle(RIWCInit e) {
        }};
    
    
//    /*events*/
//    Init
//    Crash
//    nn-aRegRead
//    nn-aRegWrite
//    bebDeliver
//    pp2pSend
//    pp2pDeliver
    
    }
    
}
