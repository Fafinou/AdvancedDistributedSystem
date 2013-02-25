package se.kth.ict.id2203.ucp.uc;

import se.sics.kompics.Init;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author ALEX & fingolfins
 */
public class UcInit extends Init{
  
    private Topology topology;
    private int maxInstance;

    public UcInit(Topology topology, int maxInstance) {
        this.topology = topology;
        this.maxInstance = maxInstance;
    }

    public Topology getTopology() {
        return topology;
    }  

    int getMaxInstance() {
      return this.maxInstance;
    }
}
