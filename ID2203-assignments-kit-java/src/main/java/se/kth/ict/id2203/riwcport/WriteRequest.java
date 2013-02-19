/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.riwcport;

import se.sics.kompics.Event;

/**
 *
 * @author ALEX
 */
public class WriteRequest extends Event{
    private int val;

    public WriteRequest(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
    
}
