package se.kth.ict.id2203.ucp.uc;

import java.util.HashSet;
import java.util.Set;
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
import se.sics.kompics.address.Address;
import se.kth.ict.id2203.ucp.uc.UcInit;

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
    private int id;
    private int proposal[];
    private int proposed[];
    private int decided[];
    
    Handler<UcInit> handleInit = new Handler<UcInit>() {
        @Override
        public void handle(UcInit e) {
            seenIds = new HashSet<Integer>();            
            seenIds.clear();
            leader=false;
        }
    };
    
    private void initInstance(int id) {
        if (!seenIds.contains(id)){
                proposal = new int[id]; 
                proposed = new int[id];
                decided = new int[id];
                proposal[id]=0;
//                proposed[id]=false;
//                decided[id]=false;
//                seenIds[id].add(id);
            
//            proposal[id]:= ⊥;
//            proposed[id] := decided[id] := false;
//            seenIds := seenIds ∪ {id};
        }
        
    }
    
    
    Handler<Trust> handleTrust = new Handler<Trust>() {
        @Override
        public void handle(Trust e) {
            if(self.equals(e.getSource())){
                leader=true;
            
            
            }
            
            
            
            
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
