package se.kth.ict.id2203.riwcport.riwcm;

import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.application.Application3b;
import se.kth.ict.id2203.bebport.BEB;
import se.kth.ict.id2203.bebport.BebBroadcast;
import se.kth.ict.id2203.pfdp.Crash;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.Pp2pSend;
import se.kth.ict.id2203.riwcport.AtomicRegister;
import se.kth.ict.id2203.riwcport.ReadRequest;
import se.kth.ict.id2203.riwcport.ReadResponse;
import se.kth.ict.id2203.riwcport.WriteRequest;
import se.kth.ict.id2203.riwcport.WriteResponse;
import se.kth.ict.id2203.riwcport.riwc.AckMessage;
import se.kth.ict.id2203.riwcport.riwc.WriteMessage;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
/**
 *
 * @author ALEX & fingolfin
 */
public class RIWCM extends ComponentDefinition{
    Negative<AtomicRegister> ar = provides(AtomicRegister.class);
    Positive<BEB> beb = requires(BEB.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application3b.class);

    public RIWCM() {
        /*subscribe(eachHandler, respective port);*/
        subscribe(handleInit, control);
        subscribe(handleReadRequest, ar);
        subscribe(handleWriteRequest, ar);
        subscribe(handleBebDeliver, beb);
        subscribe(handleAckMessage, pp2p);
    }
    private Set<Address> correct = null;
    private Address self = null;
    private int i;
    private Set<Address> writeSet = null;
    private Set<Address> readSet = null;
    private boolean reading;
    private int reqId;
    private int writeVal;
    private int v;
    private int ts;
    private int mrank;
   
    
    /*Handlers*/
    Handler<RIWCMInit> handleInit = new Handler<RIWCMInit>() {
        @Override
        public void handle(RIWCMInit e) {
            self = e.getTopology().getSelfAddress();
            //correct = e.getTopology().getNeighbors(self);
            i = self.getId();
            /*
             * initialize register 0 
             */
            writeSet = null;
            readSet = null;
            reading = false;
            reqId = 0;
            writeVal = 0;
            v = 0;
            ts = 0;
            mrank = 0;
        }
    };
    
    public boolean include(Set<Address> set1, Set<Address> set2){
        
        Iterator<Address> iter = set1.iterator();
        while (iter.hasNext()) {
            Address address = iter.next();
            if(!set2.contains(address)){
                return false;
            }
        }
        return true;
    }
    

    
    Handler<ReadRequest> handleReadRequest = new Handler<ReadRequest>() {

        @Override
        public void handle(ReadRequest e) {
            reqId = reqId + 1;
            reading = true;
            readSet.clear();
            writeSet.clear();
            //trigger(new BebBroadcast(new WriteMessage(reqId, self)), beb);
//algorithm  trigger < bebBroadcast | [Read, r, reqid[r]] >;
        }
    };
    
    Handler<WriteRequest> handleWriteRequest = new Handler<WriteRequest>() {

        @Override
        public void handle(WriteRequest e) {
            reqId = reqId + 1;
            //writeVal = val;
            readSet.clear();
            writeSet.clear();
            //trigger(new BebBroadcast(new WriteMessage(reqId, self)), beb);
//algorithm trigger < bebBroadcast | [Read, r, reqid[r]] >;
        }
    };
    
    Handler<WriteMessage> handleBebDeliver = new Handler<WriteMessage>() {
//algorithm  upon event < bebDeliver | pj , [Read, r, id] > do
        @Override
        public void handle(WriteMessage e) {
            
        //trigger(new Pp2pSend(self,.... new WriteMessage(e.getReadVal, e.getReqId(), 
        //                      ts, mrank, v, self)),pp2p);        
           
//algorithm: trigger <pp2pSend | pj , [ReadVal, r, id, (ts[r], mrank[r]), v[r]] >;
        }
    };
    
//    Handler<AckMessage> handleAckMessage = new Handler<AckMessage>() {
////algorithm   upon event < pp2pDeliver | pj , [ReadVal, r, id, (t, rk), val] > do
//        @Override
//        public void handle(AckMessage e) {
//            if(e.getId() == reqId){
////algorithm   readSet[r] := readSet[r] ∪ {((t, rk), val)};
////                readSet.add(e.getSource());
////                checkCorrectSet();
//            }
//        }

    
//        upon exists r such that |readSet[r]| > N=2 do
//        upon event < bebDeliver | pj , [Write, r, id, (t, j), val] > do
    
    
    
    
Handler<AckMessage> handleAckMessage = new Handler<AckMessage>() {
//algorithm  upon event < pp2pDeliver | pj , [Ack, r, id]> do
        @Override
        public void handle(AckMessage e) {
            if(e.getId() == reqId){
                writeSet.add(e.getSource());
                //checkCorrectSet();
            }
        }        
        


//        upon exists r such that |writeSet[r]| > N=2 do
        
        
        
    };
    
}
