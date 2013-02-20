package se.kth.ict.id2203.riwcport.riwcm;

import se.kth.ict.id2203.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class ReadValMessage extends Pp2pDeliver {

    private int reg;
    private final int id;
    private int ts;
    private int mrank;
    private int val;

    public ReadValMessage(int reg, int id, int ts, int mrank, int val, Address source) {
        super(source);
        this.reg = reg;
        this.id = id;
        this.ts = ts;
        this.mrank = mrank;
        this.val = val;
    }

    public int getReg() {
        return reg;
    }

    

    public int getMrank() {
        return mrank;
    }

    public int getId() {
        return id;
    }

    public int getTs() {
        return ts;
    }

    public int getVal() {
        return val;
    }
    
    
}