package se.kth.ict.id2203.lpbport;

import se.sics.kompics.Event;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin
 */
public class PbDeliver extends Event {
    private Address source;

    public PbDeliver(Address source) {
        this.source = source;
    }

    public Address getSource() {
        return source;
    }
    
}
