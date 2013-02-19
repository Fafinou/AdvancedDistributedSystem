/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.bebport;

import se.kth.ict.id2203.bebport.beb.BeBMessage;
import se.sics.kompics.Event;

/**
 *
 * @author ALEX
 */
public class BebBroadcast extends Event{

    private BeBMessage msg;

    public BebBroadcast(BeBMessage msg) {
        this.msg = msg;
    }
    
    public BeBMessage getBebMsg() {
        return this.msg;
    }
    
}
