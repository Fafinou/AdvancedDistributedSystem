/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.pfdp;

import se.sics.kompics.PortType;

/**
 *
 * @author fingolfin & alegeo
 */
public class PerfectFailureDetector extends PortType {
    {
        indication(Crash.class);
    }
}
