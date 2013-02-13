/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.pfdp.pfd;


import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

/**
 *
 * @author fingolfin & alegeo
 */
public class HeartBeatTimeOut extends Timeout {

    public HeartBeatTimeOut(ScheduleTimeout request) {
        super(request);
    }
}
