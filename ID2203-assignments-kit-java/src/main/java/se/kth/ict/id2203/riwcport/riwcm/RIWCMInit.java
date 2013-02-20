package se.kth.ict.id2203.riwcport.riwcm;

import se.sics.kompics.Init;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author ALEX & fingolfin
 */
public class RIWCMInit extends Init {

    private Topology topology;
    private int nbrReg;

    public RIWCMInit(Topology topology, int nbrReg) {
        this.topology = topology;
        this.nbrReg = nbrReg;
    }

    public Topology getTopology() {
        return topology;
    }

    public int getNbrReg() {
        return nbrReg;
    }
}
