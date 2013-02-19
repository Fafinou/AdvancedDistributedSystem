package se.kth.ict.id2203.riwcport;

import se.sics.kompics.PortType;

/**
 *
 * @author ALEX & fingolfin
 */
public class AtomicRegister extends PortType {
    {
        indication(ReadResponse.class);
        indication(WriteResponse.class);
        request(ReadRequest.class);
        request(WriteRequest.class);
    }
}
