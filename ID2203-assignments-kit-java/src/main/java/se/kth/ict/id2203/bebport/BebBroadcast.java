package se.kth.ict.id2203.bebport;

import se.kth.ict.id2203.riwcport.riwc.WriteMessage;
import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
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
