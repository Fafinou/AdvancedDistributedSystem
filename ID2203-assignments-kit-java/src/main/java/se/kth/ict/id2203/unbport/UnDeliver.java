/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.unbport;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin
 */
public class UnDeliver extends Event{
    Address source;

    public UnDeliver(Address source) {
        this.source = source;
    }

    public Address getSource() {
        return source;
    }
    
    
}
