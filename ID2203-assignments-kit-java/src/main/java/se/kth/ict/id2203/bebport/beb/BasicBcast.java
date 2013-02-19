/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.bebport.beb;

import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.PortType;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.text.DefaultEditorKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.application.Application2;
import se.kth.ict.id2203.bebport.BEB;
import se.kth.ict.id2203.bebport.BebBroadcast;
import se.kth.ict.id2203.bebport.BebDeliver;
import se.kth.ict.id2203.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.flp2p.Flp2pDeliver;
import se.kth.ict.id2203.flp2p.Flp2pSend;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.Pp2pSend;
import se.kth.ict.id2203.unbport.UnBroadcast;
import se.kth.ict.id2203.unbport.UnDeliver;
import se.kth.ict.id2203.unbport.UnreliableBroadcast;
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
            LoggerFactory.getLogger(Application2.class);
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
            BeBMessage toSend = e.getBebMsg();
            while (iterNodes.hasNext()) {
                Address address = iterNodes.next();
                trigger(new Pp2pSend(address, toSend),pp2p);
            }
        }
    };
    
    Handler<BeBMessage> handlePp2pDeliver = new Handler<BeBMessage>() {

        @Override
        public void handle(BeBMessage e) {
            logger.info("unreliable broadcast received a msg");
            trigger(new BebDeliver(e), beb);
        }
    };
}
