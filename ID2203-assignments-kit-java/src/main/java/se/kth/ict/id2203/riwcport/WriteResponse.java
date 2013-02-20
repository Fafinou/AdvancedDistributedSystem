package se.kth.ict.id2203.riwcport;

import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class WriteResponse extends Event{
    private int reg;

    public WriteResponse(int reg) {
        this.reg = reg;
    }

    public int getReg() {
        return reg;
    }
    
}
