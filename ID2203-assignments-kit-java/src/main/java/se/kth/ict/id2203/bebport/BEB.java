package se.kth.ict.id2203.bebport;

import se.sics.kompics.PortType;

/**
 *
 * @author ALEX & fingolfin
 */
public class BEB extends PortType {

    {
        indication(BebDeliver.class);
        request(BebBroadcast.class);
    }
}
