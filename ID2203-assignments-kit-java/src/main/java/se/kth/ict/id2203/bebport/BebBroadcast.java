package se.kth.ict.id2203.bebport;

import se.kth.ict.id2203.riwcport.riwc.WriteMessage;
import se.kth.ict.id2203.riwcport.riwcm.ReadMessage;
import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class BebBroadcast extends Event{

    private WriteMessage msg;
    private ReadMessage remsg;

    public BebBroadcast(WriteMessage writeMessage) {
        msg = writeMessage;
    }

    public BebBroadcast(ReadMessage readMessage) {
        remsg = readMessage;
    }

    public WriteMessage getMsg() {
        return msg;
    }
    
        public ReadMessage getRemsg() {
        return remsg;
    }
    
}
