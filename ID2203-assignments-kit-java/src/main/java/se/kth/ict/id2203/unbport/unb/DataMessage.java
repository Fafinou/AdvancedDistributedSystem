/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.unbport.unb;

import se.kth.ict.id2203.flp2p.Flp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin
 */
public class DataMessage extends Flp2pDeliver {
    
    private int sequenceNumber;
    
    public DataMessage(Address source, int sequenceNumber){
        super(source);
        this.sequenceNumber = sequenceNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.sequenceNumber;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataMessage other = (DataMessage) obj;
        if (this.sequenceNumber != other.sequenceNumber) {
            return false;
        }
        
        return true;
    }
 
    
}
