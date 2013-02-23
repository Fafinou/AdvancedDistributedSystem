/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.eldport.eld;

import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

/**
 *
 * @author fingolfin
 */
public class EldTimeOut extends Timeout {

    public EldTimeOut(ScheduleTimeout request) {
        super(request);
    }
    
}
