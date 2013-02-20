package se.kth.ict.id2203.riwcport.riwcm;

import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX
 */
public class TimeStampedValue {
 private int t;
    private int rk;
    private int val;

    public TimeStampedValue(int t, int rk, int val) {   
        this.t = t;
        this.rk = rk;
        this.val = val;
    }

    public int getRk() {
        return rk;
    }

    public int getT() {
        return t;
    }

    public int getVal() {
        return val;
    }
    
    
}