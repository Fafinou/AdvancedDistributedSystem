package se.kth.ict.id2203.ucp.uc;

import se.kth.ict.id2203.bebport.BebDeliver;
import se.sics.kompics.address.Address;
/**
 *
 * @author ALEX & fingolfin
 */
public class Decided extends BebDeliver {
    private final int id;
    private final Integer val;


    Decided(Address self, int id, Integer val) {
        super(self);
        this.id = id;
        this.val = val;
    }

    public int getId() {
        return id;
    }

    public Integer getVal() {
        return val;
    }
    
}
