/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.epfdp;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin & alegeo
 */
public class Restore extends Event{
    
    private Address restoredProcess;
    private int period;
    
    public Restore(Address address, int period) {
        restoredProcess = address;
        this.period = period;
    }

    public Address getRestoredProcess() {
        return restoredProcess;
    }

    public int getPeriod() {
        return period;
    }
    
    
}
