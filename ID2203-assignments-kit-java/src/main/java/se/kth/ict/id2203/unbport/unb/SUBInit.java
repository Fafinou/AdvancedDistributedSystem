/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.unbport.unb;

import se.sics.kompics.Init;
import se.sics.kompics.address.Address;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author fingolfin
 */
public class SUBInit extends Init {
    private final Topology topology;


    public SUBInit(Topology topology) {
        this.topology = topology;
 
    }

    Topology getTopology() {
        return topology;
    }
}
