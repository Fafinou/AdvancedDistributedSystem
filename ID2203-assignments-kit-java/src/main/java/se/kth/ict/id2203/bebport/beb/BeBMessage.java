package se.kth.ict.id2203.bebport.beb;

import se.kth.ict.id2203.bebport.BebDeliver;
import se.kth.ict.id2203.pp2p.Pp2pDeliver;
import se.kth.ict.id2203.riwcport.riwc.WriteMessage;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class BeBMessage extends Pp2pDeliver{
    private final BebDeliver msg;

    
    public BeBMessage(Address self, BebDeliver msg) {
        super(self);
        this.msg = msg;
    }

    public BebDeliver getMsg() {
        return msg;
    }
    
    
    
}
