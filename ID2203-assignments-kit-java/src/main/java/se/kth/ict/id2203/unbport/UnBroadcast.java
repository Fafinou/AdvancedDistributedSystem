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
public class UnBroadcast extends Event {

    private int sequenceNumber;
    private Address source;

    public UnBroadcast(int sequenceNumber, Address source) {
        this.sequenceNumber = sequenceNumber;
        this.source = source;
    }
    
    
    public int getsequenceNumber() {
        return sequenceNumber;
    }

    public Address getSource() {
        return source;
    }
    
    
}
