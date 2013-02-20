package se.kth.ict.id2203.riwcport;

import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class ReadResponse extends Event{
    private int reg;
    private int val;

    public ReadResponse(int reg, int val) {
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
