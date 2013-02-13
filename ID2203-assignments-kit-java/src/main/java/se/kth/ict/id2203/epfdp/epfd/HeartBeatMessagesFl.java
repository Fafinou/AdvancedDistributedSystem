/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.epfdp.epfd;

import se.kth.ict.id2203.application.Flp2pMessage;
import se.kth.ict.id2203.flp2p.Flp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin & alegeo
 */
public class HeartBeatMessagesFl extends Flp2pDeliver{
     
    public HeartBeatMessagesFl(Address source){
        super(source);
    }
}
