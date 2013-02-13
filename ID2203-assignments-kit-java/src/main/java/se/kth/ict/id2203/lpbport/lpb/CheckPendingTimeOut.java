/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.lpbport.lpb;

import java.util.Timer;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

/**
 *
 * @author fingolfin & alegeo
 */
public class CheckPendingTimeOut extends Timeout{

    public CheckPendingTimeOut(ScheduleTimeout request) {
        super(request);
    }
    
}
