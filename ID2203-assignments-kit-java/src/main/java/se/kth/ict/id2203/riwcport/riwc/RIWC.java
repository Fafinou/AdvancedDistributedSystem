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
    private int nbRegister;
    private Set<Address> correct = null;
    private Address self = null;
    private int i;
    private Set<Address>[] writeSet = null;
    private boolean[] reading;
    private int[] reqId;
    private int[] readVal;
    private int[] v;
    private int[] ts;
    private int[] mrank;
    /*Handlers*/
    Handler<RIWCInit> handleInit = new Handler<RIWCInit>() {
        @Override
        public void handle(RIWCInit e) {
            self = e.getTopology().getSelfAddress();
            correct = e.getTopology().getAllAddresses();
            i = self.getId();
            nbRegister = e.getNbRegister();
            /*
             * initialize register 0 
             */
            writeSet = new Set[nbRegister];
            reading = new boolean[nbRegister];
            reqId = new int[nbRegister];
            readVal = new int[nbRegister];
            v = new int[nbRegister];
            ts = new int[nbRegister];
            mrank = new int[nbRegister];
            for (int j = 0; j < nbRegister; j++) {
                writeSet[j] = new HashSet<Address>();
                writeSet[j].clear();
                reading[j] = false;
                reqId[j] = 0;
                readVal[j] = 0;
                v[j] = 0;
                ts[j] = 0;
                mrank[j] = 0;
            }
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
            int reg = e.getReg();
            reqId[reg] = reqId[reg] + 1;
            reading[reg] = true;
            writeSet[reg].clear();
            readVal[reg] = v[reg];
            logger.info("sending a write message after read request");
            trigger(new BebBroadcast(
                        new WriteMessage(
                            reg,
                            reqId[reg],
                            ts[reg],
                            mrank[reg],
                            v[reg],
                            self)),
                    beb);
        }
    };
    Handler<WriteRequest> handleWriteRequest = new Handler<WriteRequest>() {
        @Override
        public void handle(WriteRequest e) {
            int reg = e.getReg();
            reqId[reg] = reqId[reg] + 1;
            writeSet[reg].clear();
            trigger(new BebBroadcast(
                        new WriteMessage(
                            reg,
                            reqId[reg],
                            ts[reg] + 1,
                            i,
                            e.getVal(),
                            self)),
                    beb);
        }
    };
    Handler<WriteMessage> handleBebDeliver = new Handler<WriteMessage>() {
        @Override
        public void handle(WriteMessage e) {
            int reg = e.getReg();
            if ((e.getTs() > ts[reg]) && (e.getMrank() > mrank[reg])) {
                v[reg] = e.getV();
                ts[reg] = e.getTs();
                mrank[reg] = e.getMrank();
            }
            trigger(new Pp2pSend(e.getSource(), new AckMessage(self, e.getReqId(), reg)), pp2p);
        }
    };
    Handler<AckMessage> handleAckMessage = new Handler<AckMessage>() {
        @Override
        public void handle(AckMessage e) {
            int reg = e.getReg();
            if (e.getId() == reqId[reg]) {
                writeSet[reg].add(e.getSource());
                checkCorrectSet(reg);
            }
        }
    };

    private void checkCorrectSet(int reg) {

        if (include(correct, writeSet[reg])) {
            if (reading[reg]) {
                reading[reg] = false;
                trigger(new ReadResponse(readVal[reg]), ar);
            } else {
                trigger(new WriteResponse(), ar);
            }
        }
    }
}
