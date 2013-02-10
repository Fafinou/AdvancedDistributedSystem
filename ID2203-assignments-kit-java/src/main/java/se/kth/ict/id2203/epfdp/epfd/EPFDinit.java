/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.epfdp.epfd;

import se.sics.kompics.Init;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author fingolfin
 */
public class EPFDinit extends Init {
    private final Topology topology;
    private final int timeDelay;
    private final int periodIncreaser;
    private boolean fairLoss;

    public EPFDinit(Topology topology, int timeDelay, int periodIncreaser, boolean fairLoss) {
        this.topology = topology;
        this.timeDelay = timeDelay;
        this.periodIncreaser = periodIncreaser;
    }

    public Topology getTopology() {
        return topology;
    }

    public int getTimeDelay() {
        return timeDelay;
    }

    public int getPeriodIncreaser() {
        return periodIncreaser;
    }

    boolean getFairLoss() {
        return fairLoss;
    }
    
    
}
