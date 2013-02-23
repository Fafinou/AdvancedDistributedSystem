/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.eldport.eld;

import se.sics.kompics.Init;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author fingolfin
 */
public class ELDInit extends Init {
    private Topology topology;
    private int delta;


    public ELDInit(Topology topology, int delta) {
        this.topology = topology;
        this.delta = delta;
    }
    
    

    public Topology getTopology() {
        return topology;
    }

    int getDelta() {
        return this.delta;
    }
    
    
}
