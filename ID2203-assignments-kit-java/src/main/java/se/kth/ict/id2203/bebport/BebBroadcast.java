/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.bebport;

import se.kth.ict.id2203.bebport.beb.BeBMessage;
import se.kth.ict.id2203.riwcport.riwc.WriteMessage;
import se.sics.kompics.Event;

/**
 *
 * @author ALEX
 */
public class BebBroadcast extends Event{

    private WriteMessage msg;


    public BebBroadcast(WriteMessage writeMessage) {
        msg = writeMessage;
    }

    public WriteMessage getMsg() {
        return msg;
    }
    
    
    
}
