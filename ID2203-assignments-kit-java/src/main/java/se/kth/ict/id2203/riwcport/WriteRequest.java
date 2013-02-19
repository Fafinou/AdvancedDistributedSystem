package se.kth.ict.id2203.riwcport;

import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class WriteRequest extends Event{
    private int val;

    public WriteRequest(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
    
}
