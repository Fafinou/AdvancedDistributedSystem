package se.kth.ict.id2203.riwcport.riwcm;

import se.kth.ict.id2203.bebport.BebDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class ReadMessage extends BebDeliver {

    private int reg;
    private int reqId;

    public ReadMessage(int reg, int reqId, Address source) {
        super(source);
        this.reg = reg;
        this.reqId = reqId;
    }

    public int getReg() {
        return reg;
    }

    public int getReqId() {
        return reqId;
    }
}
