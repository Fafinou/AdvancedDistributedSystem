/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.eldport;

import se.sics.kompics.PortType;

/**
 *
 * @author fingolfin
 */
public class EventualLeaderDetection extends PortType {
    {
        indication(Trust.class);
    }
}
