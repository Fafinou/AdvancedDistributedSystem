/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.unbport;

import se.sics.kompics.PortType;

/**
 *
 * @author fingolfin & alegeo
 */
public class UnreliableBroadcast extends PortType{
    {
        indication(UnDeliver.class);
        request(UnBroadcast.class);
    }
}
