package se.kth.ict.id2203.riwcport;

import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class ReadResponse extends Event{
    private int val;

    public ReadResponse(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
    
}
