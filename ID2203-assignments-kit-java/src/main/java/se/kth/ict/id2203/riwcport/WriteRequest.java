package se.kth.ict.id2203.riwcport;

import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class WriteRequest extends Event{
    private int reg;
    private int val;

    public WriteRequest(int reg, int val) {
        this.reg = reg;
        this.val = val;
    }

    public int getReg() {
        return reg;
    }

    
    public int getVal() {
        return val;
    }
    
}
