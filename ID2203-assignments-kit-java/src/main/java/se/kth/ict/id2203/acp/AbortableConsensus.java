package se.kth.ict.id2203.acp;

import se.sics.kompics.PortType;

/**
 *
 * @author ALEX & fingolfin
 */
public class AbortableConsensus extends PortType {
    {
        indication(AcDecide.class);
        request(AcPropose.class);
    }
}
