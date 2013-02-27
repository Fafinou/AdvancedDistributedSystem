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
    private int timeDelay;


    public ELDInit(Topology topology, int delta, int timeDelay) {
        this.topology = topology;
        this.delta = delta;
        this.timeDelay = timeDelay;
    }
    
    

    public Topology getTopology() {
        return this.topology;
    }

    int getDelta() {
        return this.delta;
    }

    int getTimeDelay() {
        return this.timeDelay;
    }
    
    
}
