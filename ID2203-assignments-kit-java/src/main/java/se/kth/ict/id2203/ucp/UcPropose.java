package se.kth.ict.id2203.ucp;

import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class UcPropose extends Event{
    private int id;
    private int val;

    public UcPropose(int id, int val) {
        this.id = id;
        this.val = val;
    }
    
    public int getId() {
        return this.id;
    }

    public int getVal() {
        return this.val;
    }
    
}
