package se.kth.ict.id2203.acp;

import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class AcPropose extends Event{
    private int id;
    private int val;

    public int getId() {
        return id;
    }

    public int getVal() {
        return val;
    }
    
}
