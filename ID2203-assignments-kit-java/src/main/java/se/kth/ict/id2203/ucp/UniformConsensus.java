/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.ucp;

import se.sics.kompics.PortType;

/**
 *
 * @author fingolfin
 */
public class UniformConsensus extends PortType{
    {
        indication(UcDecide.class);
        request(UcPropose.class);
    }
}
