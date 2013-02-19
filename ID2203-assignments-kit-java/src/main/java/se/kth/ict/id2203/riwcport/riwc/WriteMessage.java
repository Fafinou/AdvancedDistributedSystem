/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.riwcport.riwc;

import se.kth.ict.id2203.bebport.BebDeliver;
import se.kth.ict.id2203.bebport.beb.BeBMessage;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin
 */
public class WriteMessage extends BebDeliver {

    private int reqId;
    private int ts;
    private int mrank;
    private int v;

    public WriteMessage(int reqId, int ts, int mrank, int v, Address source) {
        super(source);
        this.reqId = reqId;
        this.ts = ts;
        this.mrank = mrank;
        this.v = v;
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
