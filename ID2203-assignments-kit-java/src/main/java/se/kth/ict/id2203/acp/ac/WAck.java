/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.acp.ac;

import se.kth.ict.id2203.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin
 */
public class WAck extends Pp2pDeliver{
    private final int ts;
    private final int id;

    WAck(Address self, int id, int ts) {
        super(self);
        this.id = id;
        this.ts = ts;
        
    }

    public int getId() {
        return id;
    }

    public int getTs() {
        return ts;
    }
    
    
    
}
