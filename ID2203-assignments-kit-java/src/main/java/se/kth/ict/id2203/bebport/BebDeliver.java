/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.bebport;

import se.kth.ict.id2203.bebport.beb.BeBMessage;
import se.kth.ict.id2203.riwcport.riwc.WriteMessage;
import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX
 */
public class BebDeliver extends Event {

    

    private Address source;
    public BebDeliver(Address source) {
        this.source = source;
    }
    
    
}
