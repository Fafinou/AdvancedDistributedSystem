package se.kth.ict.id2203.ucp;

import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class UcDecide extends Event{
    private final Integer val;
    private final int id;

    public UcDecide(int id, Integer v) {
        this.id = id;
        this.val = v;
    }

    public int getId() {
        return id;
    }

    public Integer getVal() {
        return val;
    }
}
