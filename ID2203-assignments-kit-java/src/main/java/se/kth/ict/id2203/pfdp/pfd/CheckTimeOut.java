/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.pfdp.pfd;

import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

/**
 *
 * @author fingolfin
 */
public final class CheckTimeOut extends Timeout {
    
    public CheckTimeOut(ScheduleTimeout request) {
        super(request);
    }
    
}