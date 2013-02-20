package se.kth.ict.id2203.riwcport.riwc;

import se.kth.ict.id2203.bebport.BebDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class WriteMessage extends BebDeliver {

    private int reg;
    private int reqId;
    private int ts;
    private int mrank;
    private int v;

    public WriteMessage(int reg, int reqId, int ts, int mrank, int v, Address source) {
        super(source);
        this.reg = reg;
        this.reqId = reqId;
        this.ts = ts;
        this.mrank = mrank;
        this.v = v;
    }

    public int getReg() {
        return reg;
    }


    public int getMrank() {
        return mrank;
    }

    public int getReqId() {
        return reqId;
    }

    public int getTs() {
        return ts;
    }

    public int getV() {
        return v;
    }
    
    
}
