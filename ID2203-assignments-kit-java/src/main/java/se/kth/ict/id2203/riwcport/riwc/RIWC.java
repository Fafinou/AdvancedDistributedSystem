package se.kth.ict.id2203.riwcport.riwc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.application.Application3a;
import se.kth.ict.id2203.bebport.BEB;
import se.kth.ict.id2203.bebport.BebBroadcast;
import se.kth.ict.id2203.bebport.beb.BeBMessage;
import se.kth.ict.id2203.pfdp.Crash;
import se.kth.ict.id2203.pfdp.PerfectFailureDetector;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.Pp2pSend;
import se.kth.ict.id2203.riwcport.AtomicRegister;
import se.kth.ict.id2203.riwcport.ReadRequest;
import se.kth.ict.id2203.riwcport.ReadResponse;
import se.kth.ict.id2203.riwcport.WriteRequest;
import se.kth.ict.id2203.riwcport.WriteResponse;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class RIWC extends ComponentDefinition {

    Negative<AtomicRegister> ar = provides(AtomicRegister.class);
    Positive<BEB> beb = requires(BEB.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    Positive<PerfectFailureDetector> pfd = requires(PerfectFailureDetector.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application3a.class);

    public RIWC() {
        /*subscribe(eachHandler, respective port);*/
        subscribe(handleInit, control);
        subscribe(handleCrash, pfd);
        subscribe(handleReadRequest, ar);
        subscribe(handleWriteRequest, ar);
        subscribe(handleBebDeliver, beb);
        subscribe(handleAckMessage, pp2p);
    }
    private Set<Address> correct = null;
    private Address self = null;
    private int i;
    private Set<Address> writeSet = null;
    private boolean reading;
    private int reqId;
    private int readVal;
    private int v;
    private int ts;
    private int mrank;
    /*Handlers*/
    Handler<RIWCInit> handleInit = new Handler<RIWCInit>() {
        @Override
        public void handle(RIWCInit e) {
            self = e.getTopology().getSelfAddress();
            correct = e.getTopology().getAllAddresses();
            i = self.getId();
            /*
             * initialize register 0 
             */
            writeSet = new HashSet<Address>();
            writeSet.clear();
            reading = false;
            reqId = 0;
            readVal = 0;
            v = 0;
            ts = 0;
            mrank = 0;
        }
    };

    public boolean include(Set<Address> set1, Set<Address> set2) {

        Iterator<Address> iter = set1.iterator();
        while (iter.hasNext()) {
            Address address = iter.next();
            if (!set2.contains(address)) {
                return false;
            }
        }
        return true;
    }
    Handler<Crash> handleCrash = new Handler<Crash>() {
        @Override
        public void handle(Crash e) {
            correct.remove(e.getWhoCrashed());
        }
    };
    Handler<ReadRequest> handleReadRequest = new Handler<ReadRequest>() {
        @Override
        public void handle(ReadRequest e) {
            reqId = reqId + 1;
            reading = true;
            writeSet.clear();
            readVal = v;
            logger.info("sending a write message after read request");
            trigger(new BebBroadcast(new WriteMessage(reqId, ts, mrank, v, self)), beb);
        }
    };
    Handler<WriteRequest> handleWriteRequest = new Handler<WriteRequest>() {
        @Override
        public void handle(WriteRequest e) {
            reqId = reqId + 1;
            writeSet.clear();
            trigger(new BebBroadcast(new WriteMessage(reqId, ts + 1, i, e.getVal(), self)), beb);
        }
    };
    Handler<WriteMessage> handleBebDeliver = new Handler<WriteMessage>() {
        @Override
        public void handle(WriteMessage e) {
            if ((e.getTs() > ts) && (e.getMrank() > mrank)) {
                v = e.getV();
                ts = e.getTs();
                mrank = e.getMrank();
            }
            trigger(new Pp2pSend(e.getSource(), new AckMessage(self, e.getReqId())), pp2p);
        }
    };
    Handler<AckMessage> handleAckMessage = new Handler<AckMessage>() {
        @Override
        public void handle(AckMessage e) {
            if (e.getId() == reqId) {
                writeSet.add(e.getSource());
                checkCorrectSet();
            }
        }
    };

    private void checkCorrectSet() {
        
        if(include(correct, writeSet)) {
            if (reading) {
                reading = false;
                trigger(new ReadResponse(readVal), ar);
            } else {
                trigger(new WriteResponse(), ar);
            }
        }
    }
}
