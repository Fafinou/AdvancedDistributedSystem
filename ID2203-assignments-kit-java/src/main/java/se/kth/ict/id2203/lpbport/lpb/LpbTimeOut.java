package se.kth.ict.id2203.lpbport.lpb;

import se.sics.kompics.address.Address;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

/**
 *
 * @author fingolfin & alegeo
 */
public class LpbTimeOut extends Timeout{
    
    private Address source;
    private int sequenceNumber;
    
    public LpbTimeOut(ScheduleTimeout st, Address source,
                        int seqNbr){
        super(st);
        this.source = source;
        this.sequenceNumber = seqNbr;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public Address getSource() {
        return source;
    }
    
}
