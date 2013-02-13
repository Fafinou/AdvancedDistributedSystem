/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.pfdp;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin & alegeo
 */
public class Crash extends Event {
    
    private Address whoCrashed;
    
    public Crash(Address whoCrashed){
        this.whoCrashed = whoCrashed;
    }

    public Address getWhoCrashed() {
        return whoCrashed;
    }
    
    
}
