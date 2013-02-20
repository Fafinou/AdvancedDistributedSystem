package se.kth.ict.id2203.riwcport.riwc;

import se.kth.ict.id2203.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class AckMessage extends Pp2pDeliver {
    private int id;


    public AckMessage(Address self, int reqId) {
        super(self);
        this.id = reqId;
    }

    public int getId() {
        return id;
    }
    
    
    
}
