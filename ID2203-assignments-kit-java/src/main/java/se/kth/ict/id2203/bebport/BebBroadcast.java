package se.kth.ict.id2203.bebport;

import se.kth.ict.id2203.bebport.beb.BeBMessage;
import se.kth.ict.id2203.riwcport.riwc.WriteMessage;
import se.kth.ict.id2203.riwcport.riwcm.ReadMessage;
import se.sics.kompics.Event;

/**
 *
 * @author ALEX & fingolfin
 */
public class BebBroadcast extends Event{


    private BebDeliver msg;



    public BebBroadcast(BebDeliver msg) {
        this.msg = msg;
    }

    public BebDeliver getMsg() {
        return msg;
    }
       
}
