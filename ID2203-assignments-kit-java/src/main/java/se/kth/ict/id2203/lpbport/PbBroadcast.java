/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.lpbport;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin
 */
public class PbBroadcast extends Event{

    private Address source;
    public PbBroadcast(Address source) {
        this.source = source;
    }

    public Address getSource() {
        return source;
    }
    
    
}
