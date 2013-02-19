package se.kth.ict.id2203.bebport.beb;

import se.sics.kompics.Init;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author ALEX & fingolfin
 */
public class BasicBcastInit extends Init {

    private final Topology topology;


    public BasicBcastInit(Topology topology) {
        this.topology = topology;
 
    }

    Topology getTopology() {
        return topology;
    }
   
    
}
