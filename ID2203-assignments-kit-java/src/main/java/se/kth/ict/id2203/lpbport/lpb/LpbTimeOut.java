package se.kth.ict.id2203.lpbport.lpb;

import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

/**
 *
 * @author fingolfin
 */
public class LpbTimeOut extends Timeout{
    
    public LpbTimeOut(ScheduleTimeout time){
        super(time);
    }
    
}
