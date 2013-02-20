package se.kth.ict.id2203.bebport.beb;

import java.util.HashSet;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.application.Application3a;
import se.kth.ict.id2203.bebport.BEB;
import se.kth.ict.id2203.bebport.BebBroadcast;
import se.kth.ict.id2203.bebport.BebDeliver;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.Pp2pSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
/**
 *
 * @author ALEX
 */
public class BasicBcast extends ComponentDefinition{
    Negative<BEB> beb = provides(BEB.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application3a.class);
     public BasicBcast() {
        /*subscribe(eachHandler, respective port);*/
        subscribe(handleInit, control);
        subscribe(handleBebBroadcast, beb);
        subscribe(handlePp2pDeliver, pp2p);
    }
    private Address self;
    private HashSet<Address> allNodes = null;
    
    /*handlers*/
    Handler<BasicBcastInit> handleInit = new Handler<BasicBcastInit>() {

        @Override
        public void handle(BasicBcastInit e) {
            self = e.getTopology().getSelfAddress();
            allNodes = new HashSet<Address>(e.getTopology().getAllAddresses());
        }
    };
    
    Handler<BebBroadcast> handleBebBroadcast = new Handler<BebBroadcast>() {

        @Override
        public void handle(BebBroadcast e) {
            Iterator<Address> iterNodes = allNodes.iterator();
            BebDeliver toSend = e.getMsg();
            while (iterNodes.hasNext()) {
                Address address = iterNodes.next();
                trigger(new Pp2pSend(address,new BeBMessage(self, toSend)),pp2p);
            }
        }
    };
    
    Handler<BeBMessage> handlePp2pDeliver = new Handler<BeBMessage>() {

        @Override
        public void handle(BeBMessage e) {
            //logger.info("unreliable broadcast received a msg");
            trigger(e.getMsg(), beb);
        }
    };
}
