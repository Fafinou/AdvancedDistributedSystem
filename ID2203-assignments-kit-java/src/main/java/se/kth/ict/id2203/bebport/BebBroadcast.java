package se.kth.ict.id2203.bebport;

import se.kth.ict.id2203.bebport.beb.BeBMessage;
import se.kth.ict.id2203.riwcport.riwc.WriteMessage;
import se.kth.ict.id2203.riwcport.riwcm.ReadMessage;
import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class BebBroadcast extends Event{

<<<<<<< HEAD
    private WriteMessage msg;
    private ReadMessage remsg;
=======
    private BebDeliver msg;

>>>>>>> branch 'master' of https://github.com/Fafinou/AdvancedDistributedSystem.git

<<<<<<< HEAD
    public BebBroadcast(WriteMessage writeMessage) {
        msg = writeMessage;
    }

    public BebBroadcast(ReadMessage readMessage) {
        remsg = readMessage;
=======
    public BebBroadcast(BebDeliver msg) {
        this.msg = msg;
>>>>>>> branch 'master' of https://github.com/Fafinou/AdvancedDistributedSystem.git
    }

    public BebDeliver getMsg() {
        return msg;
    }
    
        public ReadMessage getRemsg() {
        return remsg;
    }
    
}
