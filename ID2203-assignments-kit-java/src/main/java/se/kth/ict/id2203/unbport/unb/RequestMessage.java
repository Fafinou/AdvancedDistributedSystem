/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.unbport.unb;

import se.kth.ict.id2203.flp2p.Flp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin & alegeo
 */
public class RequestMessage extends Flp2pDeliver {
    private Address requestedSource;
    private int sequenceNumber;
    private int TTL;
    
    public RequestMessage(Address source, 
            Address requestedSource, 
            int seqNbr,
            int TTL){
        super(source);
        this.requestedSource = requestedSource;
        this.sequenceNumber= seqNbr;
        this.TTL = TTL;
    }

    public Address getRequestedSource() {
        return requestedSource;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getTTL() {
        return TTL;
    }
    
    
}
