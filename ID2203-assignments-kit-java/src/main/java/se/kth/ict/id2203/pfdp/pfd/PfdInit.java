/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.pfdp.pfd;

import se.sics.kompics.Init;
import se.sics.kompics.address.Address;
import se.sics.kompics.launch.Topology;

/**
 * 
 * @author fingolfin & alegeo
 */
public class PfdInit extends Init {

    private final Topology topology;
    private final int gamma;
    private final int delta; 


    /**
     *
     * @param topology
     */
    public PfdInit(Topology topology, int gamma, int delta) {
        this.topology = topology;
        this.gamma = gamma;
        this.delta = delta;

    }

    
    public int getGamma() {
        return gamma;
    }

    public int getDelta() {
        return delta;
    }

    public final Topology getTopology() {
        return topology;
    }
}
