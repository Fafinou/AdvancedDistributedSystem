package se.kth.ict.id2203.ucp.uc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.acp.AbortableConsensus;
import se.kth.ict.id2203.acp.AcDecide;
import se.kth.ict.id2203.application.Application4;
import se.kth.ict.id2203.bebport.BEB;
import se.kth.ict.id2203.eldport.EventualLeaderDetection;
import se.kth.ict.id2203.eldport.Trust;
import se.kth.ict.id2203.ucp.UcPropose;
import se.kth.ict.id2203.ucp.UniformConsensus;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;

/**
 *
 * @author ALEX & fingolfin
 */
public class PaxosUniformConsensus extends ComponentDefinition {

    Negative<UniformConsensus> uc = provides(UniformConsensus.class);
    Positive<BEB> beb = requires(BEB.class);
    Positive<AbortableConsensus> ac = requires(AbortableConsensus.class);
    Positive<EventualLeaderDetection> eld = requires(EventualLeaderDetection.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application4.class);

    public PaxosUniformConsensus() {
        /*subscribe(eachHandler, respective port);*/
        subscribe(handleInit, control);
        subscribe(handleTrust, eld);
        subscribe(handleUcPropose, uc);
        subscribe(handleAcReturn, ac);
        subscribe(handleDecided, beb);
    }
    Handler<UcInit> handleInit = new Handler<UcInit>() {
        @Override
        public void handle(UcInit e) {
        }
    };
    Handler<Trust> handleTrust = new Handler<Trust>() {
        @Override
        public void handle(Trust e) {
        }
    };
    Handler<UcPropose> handleUcPropose = new Handler<UcPropose>() {
        @Override
        public void handle(UcPropose e) {
        }
    };
    Handler<AcDecide> handleAcReturn = new Handler<AcDecide>() {
        @Override
        public void handle(AcDecide e) {
        }
    };
    Handler<Decided> handleDecided = new Handler<Decided>() {
        @Override
        public void handle(Decided e) {
        }
    };
}
