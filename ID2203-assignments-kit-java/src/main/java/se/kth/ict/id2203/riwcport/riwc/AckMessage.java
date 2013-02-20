package se.kth.ict.id2203.riwcport.riwc;

import se.kth.ict.id2203.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class AckMessage extends Pp2pDeliver {
    private int reg;
    private int id;
    

    
    AckMessage(Address self, int reqId, int reg) {
        super(self);
        this.id = reqId;
        this.reg = reg;
    }

    public int getId() {
        return id;
    }

    public int getReg() {
        return reg;
    }
    
    
    
    
}
