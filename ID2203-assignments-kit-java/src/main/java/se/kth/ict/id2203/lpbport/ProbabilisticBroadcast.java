/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.lpbport;

import se.sics.kompics.PortType;

/**
 *
 * @author fingolfin & alegeo
 */
public class ProbabilisticBroadcast extends PortType {
    {
        indication(PbDeliver.class);
        request(PbBroadcast.class);
    }
    
}
