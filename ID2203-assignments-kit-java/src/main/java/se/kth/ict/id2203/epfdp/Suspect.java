/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.epfdp;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin
 */
public class Suspect extends Event {

    private Address suspectedProcess;
    private int period;
    
    public Suspect(Address address, int period) {
        suspectedProcess = address;
        this.period = period;
    }

    public Address getSuspectedProcess() {
        return suspectedProcess;
    }

    public int getPeriod() {
        return period;
    }
    
}
