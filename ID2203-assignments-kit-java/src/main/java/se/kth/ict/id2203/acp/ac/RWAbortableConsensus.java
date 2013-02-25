/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.acp.ac;

import java.util.HashSet;
import java.util.Set;
import se.kth.ict.id2203.acp.AbortableConsensus;
import se.kth.ict.id2203.acp.AcPropose;
import se.kth.ict.id2203.acp.ac.*;
import se.kth.ict.id2203.acp.ac.*;
import se.kth.ict.id2203.acp.ac.*;
import se.kth.ict.id2203.acp.ac.*;
import se.kth.ict.id2203.acp.ac.*;
import se.kth.ict.id2203.bebport.BEB;
import se.kth.ict.id2203.bebport.beb.BasicBcast;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin
 */
public class RWAbortableConsensus extends ComponentDefinition {

    Negative<AbortableConsensus> ac = provides(AbortableConsensus.class);
    Positive<BEB> beb = requires(BEB.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);

    public RWAbortableConsensus() {
        /**/
    }
    
    private Set<Integer> seenId;
    private int majority;
    private Set<Address> allNodes;
    private Address self;
    
    Handler<RWACInit> handleInit = new Handler<RWACInit>() {
        @Override
        public void handle(RWACInit e) {
            seenId = new HashSet<Integer>();
            seenId.clear();
            allNodes = e.getTopology().getAllAddresses();
            self = e.getTopology().getSelfAddress();
            majority = (allNodes.size()/2) + 1;
        }
    };
    Handler<AcPropose> handlePropose = new Handler<AcPropose>() {
        @Override
        public void handle(AcPropose e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    Handler<Read> handleRead = new Handler<Read>() {
        @Override
        public void handle(Read e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    Handler<NAck> handleNack = new Handler<NAck>() {
        @Override
        public void handle(NAck e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    Handler<RAck> handleRack = new Handler<RAck>() {

        @Override
        public void handle(RAck e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    
    Handler<Write> handleWrite = new Handler<Write>() {

        @Override
        public void handle(Write e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    
    Handler<WAck> handleWack = new Handler<WAck>() {

        @Override
        public void handle(WAck e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
            
}
