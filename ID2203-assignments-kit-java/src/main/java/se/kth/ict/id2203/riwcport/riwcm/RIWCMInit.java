package se.kth.ict.id2203.riwcport.riwcm;

import se.sics.kompics.Init;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author ALEX
 */
public class RIWCMInit extends Init {
        private Topology topology;
    
    public Topology getTopology() {
        return topology;
    }

    public RIWCMInit(Topology topology) {
        this.topology = topology;
}
}
