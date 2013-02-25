package se.kth.ict.id2203.acp.ac;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import se.kth.ict.id2203.acp.AbortableConsensus;
import se.kth.ict.id2203.acp.AcDecide;
import se.kth.ict.id2203.acp.AcPropose;
import se.kth.ict.id2203.acp.ac.*;
import se.kth.ict.id2203.acp.ac.*;
import se.kth.ict.id2203.acp.ac.*;
import se.kth.ict.id2203.acp.ac.*;
import se.kth.ict.id2203.acp.ac.*;
import se.kth.ict.id2203.bebport.BEB;
import se.kth.ict.id2203.bebport.BebBroadcast;
import se.kth.ict.id2203.bebport.beb.BasicBcast;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.Pp2pSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class RWAbortableConsensus extends ComponentDefinition {

    Negative<AbortableConsensus> ac = provides(AbortableConsensus.class);
    Positive<BEB> beb = requires(BEB.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);

    public RWAbortableConsensus() {
        /**/
    }
    private Set<Integer> seenId;
    private int N;
    private int majority;
    private Set<Address> allNodes;
    private Address self;
    private int maxInstance;
    private Integer[] tempValue;
    private Integer[] val;
    private Integer[] wAcks;
    private Integer[] rts;
    private Integer[] wts;
    private Integer[] tstamp;
    private Set<AcValue>[] readSet;

    private void initInstance(int id) {
        if (!seenId.contains(id)) {
            tempValue = new Integer[maxInstance];
            tempValue[id] = -1;
            val = new Integer[maxInstance];
            val[id] = -1;

            wAcks = new Integer[maxInstance];
            rts = new Integer[maxInstance];
            wts = new Integer[maxInstance];
            wAcks[id] = 0;
            rts[id] = 0;
            wts[id] = 0;
            
            tstamp = new Integer[maxInstance];
            tstamp[id] = self.getId();

            readSet = new Set[maxInstance];
            readSet[id] = new HashSet<AcValue>();
            readSet[id].clear();

            seenId.add(id);

        }
    }

    private AcValue highest(Set<AcValue> set) {
        AcValue toReturn = new AcValue(-1, null);
        Iterator<AcValue> iter = set.iterator();
        while (iter.hasNext()) {
            AcValue val = iter.next();
            if(val.ts > toReturn.ts){
                toReturn = val;
            }
        }
        return toReturn;
    }
    
    Handler<RWACInit> handleInit = new Handler<RWACInit>() {
        @Override
        public void handle(RWACInit e) {
            seenId = new HashSet<Integer>();
            seenId.clear();
            allNodes = e.getTopology().getAllAddresses();
            self = e.getTopology().getSelfAddress();
            N = allNodes.size();
            majority = (N / 2) + 1;
            maxInstance = e.getMaxInstance();
        }
    };
    Handler<AcPropose> handlePropose = new Handler<AcPropose>() {
        @Override
        public void handle(AcPropose e) {
            int id = e.getId();
            initInstance(id);
            tstamp[id] = tstamp[id] + N;
            tempValue[id] = e.getVal();
            trigger(new BebBroadcast(new Read(self, id, tstamp[id])), beb);
        }
    };
    Handler<Read> handleRead = new Handler<Read>() {
        @Override
        public void handle(Read e) {
            int id = e.getId();
            int ts = e.getTstamp();
            initInstance(id);
            if (rts[id] >= ts || wts[id] >= ts) {
                trigger(new Pp2pSend(e.getSource(), new NAck(self, id)), pp2p);
            } else {
                rts[id] = ts;
                trigger(new Pp2pSend(e.getSource(), new RAck(self, id, wts[id], val[id], ts)), pp2p);
            }
        }
    };
    Handler<NAck> handleNack = new Handler<NAck>() {
        @Override
        public void handle(NAck e) {
            int id = e.getId();
            readSet[id].clear();
            wAcks[id] = 0;
            trigger(new AcDecide(id, null), ac);
        }
    };
    Handler<RAck> handleRack = new Handler<RAck>() {
        @Override
        public void handle(RAck e) {
            int id = e.getId();
            int ts = e.getTs();
            Integer v = e.getVal();
            int sentts = e.getWts();
            if (sentts == tstamp[id]) {
                readSet[id].add(new AcValue(ts, v));
                if (readSet[id].size() == majority) {
                    AcValue acVal = highest(readSet[id]);
                    if (!v.equals(null)) {
                        tempValue[id] = acVal.val;
                    }
                    trigger(new BebBroadcast(new Write(self, id, tstamp[id], tempValue[id])), beb);
                }
            }
        }
    };
    Handler<Write> handleWrite = new Handler<Write>() {
        @Override
        public void handle(Write e) {
            int id = e.getId();
            int ts = e.getTs();
            int v = e.getVal();
            initInstance(id);
            if(rts[id] > ts || wts[id] > ts){
                trigger(new Pp2pSend(e.getSource(), new NAck(self, id)), pp2p);
            }else{
                val[id] = v;
                wts[id] = ts;
                trigger(new Pp2pSend(e.getSource(), new WAck(self, id, ts)), pp2p);
            }
        }
    };
    Handler<WAck> handleWack = new Handler<WAck>() {
        @Override
        public void handle(WAck e) {
            int id = e.getId();
            int sentts = e.getTs();
            if(sentts == tstamp[id]){
                wAcks[id] = wAcks[id] + 1;
                if(wAcks[id] == majority){
                    readSet[id].clear();
                    wAcks[id] = 0;
                    trigger(new AcDecide(id, tempValue[id]), ac);
                }
            }
        }
    };
}
