package se.kth.ict.id2203.riwcport.riwcm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.application.Application3a;
import se.kth.ict.id2203.bebport.BEB;
import se.kth.ict.id2203.bebport.BebBroadcast;
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
public class RIWCM extends ComponentDefinition {

    Negative<AtomicRegister> ar = provides(AtomicRegister.class);
    Positive<BEB> beb = requires(BEB.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application3a.class);

    public RIWCM() {
        /*subscribe(eachHandler, respective port);*/
        subscribe(handleInit, control);
        subscribe(handleReadRequest, ar);
        subscribe(handleWriteRequest, ar);
        subscribe(handleReadMessage, beb);
        subscribe(handleReadValMessage, pp2p);
        subscribe(handleWriteMessage, beb);
        subscribe(handleAckMessage, pp2p);
    }
    private Set<Address> correct = null;
    private Address self = null;
    private int nbRegister;
    private int i;
    private Set<Address>[] writeSet = null;
    private Set<TimeStampedValue>[] readSet = null;
    private boolean reading[];
    private int reqId[];
    private int writeVal[];
    private int readVal[];
    private int v[];
    private int ts[];
    private int mrank[];
    private int N;
    /*Handlers*/
    Handler<RIWCMInit> handleInit = new Handler<RIWCMInit>() {
        @Override
        public void handle(RIWCMInit e) {
            self = e.getTopology().getSelfAddress();
            N = e.getTopology().getAllAddresses().size();
            i = self.getId();
            nbRegister = e.getNbrReg();
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
            readSet = new Set[nbRegister];
            writeVal = new int[nbRegister];
            for (int j = 0; j < nbRegister; j++) {
                writeSet[j] = new HashSet<Address>();
                writeSet[j].clear();
                readSet[j] = new HashSet<TimeStampedValue>();
                readSet[j].clear();
                reading[j] = false;
                reqId[j] = 0;
                readVal[j] = 0;
                writeVal[j] = 0;
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

    public TimeStampedValue highest(Set<TimeStampedValue> readset1) {
        return Collections.max(readset1);
    }
    Handler<ReadRequest> handleReadRequest = new Handler<ReadRequest>() {
        @Override
        public void handle(ReadRequest e) {
            int reg = e.getReg();
            reqId[reg] = reqId[reg] + 1;
            reading[reg] = true;
            readSet[reg].clear();
            writeSet[reg].clear();
            trigger(new BebBroadcast(new ReadMessage(reg, reqId[reg], self)), beb);
            //algorithm  trigger < bebrBroadcast | [Read, r, reqid[r]] >;
        }
    };
    Handler<WriteRequest> handleWriteRequest = new Handler<WriteRequest>() {
        @Override
        public void handle(WriteRequest e) {
            int reg = e.getReg();
            reqId[reg] = reqId[reg] + 1;
            writeVal[reg] = e.getVal();
            readSet[reg].clear();
            writeSet[reg].clear();
            trigger(new BebBroadcast(new ReadMessage(reg, reqId[reg], self)), beb);
        }
    };
    Handler<ReadMessage> handleReadMessage = new Handler<ReadMessage>() {
        @Override
        public void handle(ReadMessage e) {
            int reg = e.getReg();
            trigger(new Pp2pSend(e.getSource(), new ReadValMessage(reg, e.getReqId(), ts[reg], mrank[reg], v[reg], self)), pp2p);
        }
    };
    Handler<ReadValMessage> handleReadValMessage = new Handler<ReadValMessage>() {
        @Override
        public void handle(ReadValMessage e) {
            int reg = e.getReg();
            if (e.getId() == reqId[reg]) {
                readSet[reg].add(new TimeStampedValue(e.getTs(), e.getMrank(), e.getVal()));
                checkReadSet(reg);
            }
        }
    };
    Handler<WriteMessage> handleWriteMessage = new Handler<WriteMessage>() {
//algorithm    upon event < bebDeliver | pj , [Write, r, id, (t, j), val] > 
        @Override
        public void handle(WriteMessage e) {
            int reg = e.getReg();
            if (compareTimeStamp(e.getTs(), e.getMrank(), ts[reg], mrank[reg])) {
                //if ((e.getTs() > ts[reg]) && (e.getMrank() > mrank[reg])) {
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
                checkWriteSet(reg);
            }
        }
    };

    /**
     * returns true iff (ts1,rk1) > (ts2,rk2)
     */
    private boolean compareTimeStamp(int ts1, int rk1, int ts2, int rk2) {
        if (ts1 > ts2) {
            return true;
        } else if (ts1 == ts2) {
            if (rk1 > rk2) {
                return true;
            }
        }
        return false;
    }

    private void checkWriteSet(int j) {
        if (writeSet[j].size() > (N/2)) {
            if (reading[j]) {
                reading[j] = false;
                trigger(new ReadResponse(j, readVal[j]), ar);
            } else {
                trigger(new WriteResponse(j), ar);
            }

        }
    }

    private void checkReadSet(int j) {
        if (readSet[j].size() > (N / 2)) {
            TimeStampedValue value = highest(readSet[j]);
            readVal[j] = value.getVal();
            if (reading[j]) {
                trigger(new BebBroadcast(new WriteMessage(j, reqId[j], value.getT(), value.getRk(), readVal[j], self)), beb);
            } else {
                trigger(new BebBroadcast(new WriteMessage(j, reqId[j], value.getT() + 1, i, writeVal[j], self)), beb);
            }
        }
    }
}