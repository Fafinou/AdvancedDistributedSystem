/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.bebport;

import se.kth.ict.id2203.bebport.BebDeliver;
import se.kth.ict.id2203.bebport.BebBroadcast;
import se.sics.kompics.PortType;

/**
 *
 * @author ALEX
 */
public class BEB extends PortType {

    {
        indication(BebDeliver.class);
        request(BebBroadcast.class);
    }
}
