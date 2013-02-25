package se.kth.ict.id2203.acp.ac;

import se.kth.ict.id2203.bebport.BebDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class Write extends BebDeliver{
    private final int id;
    private final Integer ts;
    private final Integer val;


    Write(Address self, int id, Integer ts, Integer val) {
        super(self);
        this.id = id;
        this.ts = ts;
        this.val = val;
    }

    public int getId() {
        return id;
    }

    public Integer getTs() {
        return ts;
    }

    public Integer getVal() {
        return val;
    }
    
}
