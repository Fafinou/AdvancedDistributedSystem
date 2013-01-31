/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.pfdp.pfd;

import se.kth.ict.id2203.pp2p.Pp2pDeliver;
import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin
 */
public class HeartBeatMessage extends Pp2pDeliver{
    
    public HeartBeatMessage(Address source){
        super(source);
    }
}
