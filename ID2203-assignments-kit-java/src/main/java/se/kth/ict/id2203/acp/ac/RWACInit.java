/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.acp.ac;

import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author ALEX & fingolfin
 */
public class RWACInit extends Init {
      
    private Topology topology;
    private int maxInstance;
   

    public RWACInit(Topology topology, int maxInstance) {
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
