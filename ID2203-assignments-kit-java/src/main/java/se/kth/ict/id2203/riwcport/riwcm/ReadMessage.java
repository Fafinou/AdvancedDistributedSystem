package se.kth.ict.id2203.riwcport.riwcm;

import se.kth.ict.id2203.bebport.BebDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class ReadMessage extends BebDeliver  {
    private int reqId;
    
    public ReadMessage(int reqId, Address source) {
        super(source);
        this.reqId = reqId;
    }  
        public int getReqId() {
        return reqId;
    }
}
