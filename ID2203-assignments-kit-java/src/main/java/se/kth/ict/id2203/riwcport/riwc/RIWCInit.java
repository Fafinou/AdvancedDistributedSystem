package se.kth.ict.id2203.riwcport.riwc;

import se.sics.kompics.Init;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author ALEX & fingolfin
 */
public class RIWCInit extends Init{
    private Topology topology;
    
    public Topology getTopology() {
        return topology;
    }

    public RIWCInit(Topology topology) {
        this.topology = topology;
}
}