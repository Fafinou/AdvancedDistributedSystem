package se.kth.ict.id2203.bebport.beb;

import se.kth.ict.id2203.pp2p.Pp2pDeliver;
import se.kth.ict.id2203.riwcport.riwc.WriteMessage;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class BeBMessage extends Pp2pDeliver{
    private final WriteMessage msg;

    
    public BeBMessage(Address self, WriteMessage msg) {
        super(self);
        this.msg = msg;
    }

    public WriteMessage getMsg() {
        return msg;
    }
    
    
    
}
