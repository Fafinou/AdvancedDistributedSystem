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
public class RAck extends Pp2pDeliver {
    private final int ts;
    private final Integer val;
    private final Integer wts;
    private final int id;

    RAck(Address self, int id, Integer wts, Integer val, int ts) {
        super(self);
        this.id = id;
        this.wts = wts;
        this.val = val;
        this.ts = ts;
        
    }

    public int getId() {
        return id;
    }

    public int getTs() {
        return ts;
    }

    public Integer getVal() {
        return val;
    }

    public Integer getWts() {
        return wts;
    }
    
    
    
}
