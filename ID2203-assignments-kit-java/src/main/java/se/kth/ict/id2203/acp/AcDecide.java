package se.kth.ict.id2203.acp;

import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class AcDecide extends Event {
    private final int id;
    private final Integer val;

    public AcDecide(int id, Integer val) {
        this.id = id;
        this.val = val;
    }

    public int getId() {
        return id;
    }

    public Integer getVal() {
        return val;
    }
    
}
