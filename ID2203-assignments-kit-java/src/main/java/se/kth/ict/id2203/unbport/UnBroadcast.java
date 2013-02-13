/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.unbport;

import se.kth.ict.id2203.unbport.unb.DataMessage;
import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin & alegeo
 */
public class UnBroadcast extends Event {


    private DataMessage dataMsg;

    /*
    public UnBroadcast(int sequenceNumber, Address source, String msg) {
        this.sequenceNumber = sequenceNumber;
        this.source = source;
        this.msg = msg;
    }
    */

    public UnBroadcast(DataMessage dataMsg) {
        this.dataMsg = dataMsg;
    }

    public DataMessage getDataMsg() {
        return dataMsg;
    }
    
    
}
