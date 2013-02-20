package se.kth.ict.id2203.bebport;

import java.io.Serializable;
import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public abstract class BebDeliver extends Event implements Serializable{

    private Address source;

    public BebDeliver(Address source) {
        this.source = source;
    }

    public Address getSource() {
        return source;
    }
    
    
}
