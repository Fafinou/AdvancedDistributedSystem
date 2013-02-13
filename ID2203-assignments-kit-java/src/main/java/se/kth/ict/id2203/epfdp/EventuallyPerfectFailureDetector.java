/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.epfdp;

import se.sics.kompics.PortType;

/**
 *
 * @author fingolfin & alegeo
 */
public class EventuallyPerfectFailureDetector extends PortType{
    {
        indication(Restore.class);
        indication(Suspect.class);
    }
}
