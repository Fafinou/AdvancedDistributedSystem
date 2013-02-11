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
    private double alpha;
    private int R;
    private int delay;

    public Topology getTopology() {
        return topology;
    }

    public LazyPbInit(Topology topology, int fanOut, double alpha, int R, int delay) {
        this.topology = topology;
        this.fanOut = fanOut;
        this.alpha = alpha;
        this.R = R;
        this.delay = delay;
    }

    int getFanOut() {
        return fanOut;
    }

    double getAlpha() {
        return this.alpha;
    }

    int getR() {
        return this.R;
    }

    int getDelay() {
        return this.delay;
    }
    
}
