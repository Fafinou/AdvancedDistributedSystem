/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.riwcport.riwc;

import se.kth.ict.id2203.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin
 */
public class AckMessage extends Pp2pDeliver {

    public AckMessage(Address source) {
        super(source);
    }
    
}
