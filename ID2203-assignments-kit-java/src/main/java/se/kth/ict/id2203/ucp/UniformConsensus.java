package se.kth.ict.id2203.ucp;

import se.sics.kompics.PortType;

/**
 *
 * @author ALEX & fingolfin
 */
public class UniformConsensus extends PortType{
    {
        indication(UcDecide.class);
        request(UcPropose.class);
    }
}
