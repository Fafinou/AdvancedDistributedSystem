package se.kth.ict.id2203.ucp.uc;

import se.sics.kompics.Init;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author ALEX & fingolfins
 */
public class UcInit extends Init{
  
    private Topology topology;
   

    public UcInit(Topology topology) {
        this.topology = topology;
    }

    public Topology getTopology() {
        return topology;
    }  
}
