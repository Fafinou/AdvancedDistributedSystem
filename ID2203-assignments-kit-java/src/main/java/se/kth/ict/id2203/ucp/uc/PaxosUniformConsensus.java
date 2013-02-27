package se.kth.ict.id2203.ucp.uc;

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
import se.kth.ict.id2203.eldport.EventualLeaderDetection;
import se.kth.ict.id2203.eldport.Trust;
import se.kth.ict.id2203.ucp.UcDecide;
import se.kth.ict.id2203.ucp.UcPropose;
import se.kth.ict.id2203.ucp.UniformConsensus;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

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
    private Address self = null;
    private Set<Integer> seenIds = null;
    private boolean leader;
    private int maxInstance;
    private Integer proposal[];
    private boolean proposed[];
    private boolean decided[];
    Handler<UcInit> handleInit = new Handler<UcInit>() {
        @Override
        public void handle(UcInit e) {
            seenIds = new HashSet<Integer>();
            seenIds.clear();
            self = e.getTopology().getSelfAddress();
            leader = false;
            maxInstance = e.getMaxInstance();
            proposal = new Integer[maxInstance];
            proposed = new boolean[maxInstance];
            decided = new boolean[maxInstance];
        }
    };

    private void initInstance(int id) {
        if (!seenIds.contains(id)) {

            proposal[id] = null;
            proposed[id] = false;
            decided[id] = false;
            seenIds.add(id);
        }
    }
    Handler<Trust> handleTrust = new Handler<Trust>() {
        @Override
        public void handle(Trust e) {
            logger.info("we trust " + e.getLeader() + "as a leader");
            if (self.equals(e.getLeader())) {
                leader = true;
                
                Iterator<Integer> iterId = seenIds.iterator();
                while (iterId.hasNext()) {
                    int id = iterId.next();
                    tryPropose(id);
                }
            } else {
                leader = false;
            }
        }
    };
    Handler<UcPropose> handleUcPropose = new Handler<UcPropose>() {
        @Override
        public void handle(UcPropose e) {
            int id = e.getId();
            int v = e.getVal();
            initInstance(id);
            proposal[id] = v;
            logger.info("proposal is now: "+v);
            tryPropose(id);
        }
    };
    Handler<AcDecide> handleAcReturn = new Handler<AcDecide>() {
        @Override
        public void handle(AcDecide e) {
            int id = e.getId();
            Integer result = e.getVal();
            if (!(result == null)) {
                logger.info("value accepted: "+e.getVal());
                trigger(new BebBroadcast(new Decided(self, id, result)), beb);
            } else {
                logger.info("aborting proposal "+ proposal[id] +"...");
                proposed[id] = false;
                tryPropose(id);
            }
        }
    };
    Handler<Decided> handleDecided = new Handler<Decided>() {
        @Override
        public void handle(Decided e) {
            int id = e.getId();
            Integer v = e.getVal();
            initInstance(id);
            if (!decided[id]) {
                decided[id] = true;
                trigger(new UcDecide(id, v), uc);
            }
        }
    };

    private void tryPropose(int id) {

        if (leader && !proposed[id] && !(proposal[id] == null)) {
            logger.info("leader: "+self +" ... I propose");
            proposed[id] = true;
            trigger(new AcPropose(id, proposal[id]), ac);
        }
    }
}
