package se.kth.ict.id2203.riwcport;

import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class ReadRequest extends Event{
    private int reg;

    public ReadRequest(int reg) {
        this.reg = reg;
    }

    public int getReg() {
        return reg;
    }
    
}
