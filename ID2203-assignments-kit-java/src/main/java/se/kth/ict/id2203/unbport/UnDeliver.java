/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.unbport;

import se.kth.ict.id2203.unbport.unb.DataMessage;
import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin
 */
public class UnDeliver extends Event{

    private DataMessage msg;
    public UnDeliver(DataMessage msg) {
        this.msg = msg;
    }

    public DataMessage getMsg() {
        return msg;
    }
    
    

    
    
}
