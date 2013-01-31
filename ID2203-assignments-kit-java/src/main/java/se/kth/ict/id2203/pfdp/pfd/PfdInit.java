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
 * @author fingolfin
 */
public class PfdInit extends Init {

    private final Topology topology;
    private final int gamma;
    private final int delta;
    private final Address self;

    /**
     *
     * @param topology
     */
    public PfdInit(Topology topology, int gamma, int delta, Address self) {
        this.topology = topology;
        this.gamma = gamma;
        this.delta = delta;
        this.self = self;
    }

    public int getGamma() {
        return gamma;
    }

    public int getDelta() {
        return delta;
    }

    public Address getSelf() {
        return self;
    }

    
    public final Topology getTopology() {
        return topology;
    }
}
