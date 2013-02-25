package se.kth.ict.id2203.acp.ac;

import se.kth.ict.id2203.bebport.BebDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class Read extends BebDeliver{
    private final int id;
    private final Integer tstamp;

    public Read(Address self, int id, Integer tstamp) {
        super(self);
        this.id = id;
        this.tstamp = tstamp;
    }

    public int getId() {
        return id;
    }

    public Integer getTstamp() {
        return tstamp;
    }
    
    
}
