package se.kth.ict.id2203.bebport;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class BebDeliver extends Event {

    private Address source;
    public BebDeliver(Address source) {
        this.source = source;
    }
    
    
}
