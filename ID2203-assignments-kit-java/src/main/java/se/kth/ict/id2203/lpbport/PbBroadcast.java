package se.kth.ict.id2203.lpbport;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin
 */
public class PbBroadcast extends Event{

    private Address source;
    private String msg;
    public PbBroadcast(Address source, String msg) {
        this.source = source;
        this.msg = msg;
    }

    public Address getSource() {
        return source;
    }

    public String getMsg() {
        return msg;
    }
    
    
}
