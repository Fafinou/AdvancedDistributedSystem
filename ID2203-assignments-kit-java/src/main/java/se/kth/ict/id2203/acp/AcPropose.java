package se.kth.ict.id2203.acp;

import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class AcPropose extends Event{
    private int id;
    private Integer val;

    public AcPropose(int id, Integer val) {
        this.id = id;
        this.val = val;
    }

    public int getId() {
        return id;
    }

    public int getVal() {
        return val;
    }
    
}
