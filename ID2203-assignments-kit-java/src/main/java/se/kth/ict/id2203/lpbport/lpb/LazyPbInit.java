package se.kth.ict.id2203.lpbport.lpb;

import se.sics.kompics.Init;
import se.sics.kompics.launch.Topology;
import sun.java2d.pipe.AlphaColorPipe;

/**
 *
 * @author fingolfin
 */
public class LazyPbInit extends Init {
    private Topology topology;
    private int fanOut;
    private double storeThreshold;
    private int maxRound;
    private int delay;

    public Topology getTopology() {
        return topology;
    }

    public LazyPbInit(Topology topology, int fanOut, double storeThreshold, int maxRound, int delay) {
        this.topology = topology;
        this.fanOut = fanOut;
        this.storeThreshold = storeThreshold;
        this.maxRound = maxRound;
        this.delay = delay;
    }

    int getFanOut() {
        return fanOut;
    }

    double getStoreThreshold() {
        return this.storeThreshold;
    }

    int getMaxRound() {
        return this.maxRound;
    }

    int getDelay() {
        return this.delay;
    }
    
}
