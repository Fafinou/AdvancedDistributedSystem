/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.unbport.unb;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.application.Application2;
import se.kth.ict.id2203.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.flp2p.Flp2pDeliver;
import se.kth.ict.id2203.flp2p.Flp2pSend;
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
 * @author fingolfin & alegeo
 */
public class SUB extends ComponentDefinition{
    Negative<UnreliableBroadcast> ubp = provides(UnreliableBroadcast.class);
    Positive<FairLossPointToPointLink> flp2p = requires(FairLossPointToPointLink.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application2.class);
     public SUB() {
        /*subscribe(eachHandler, respective port);*/
        subscribe(handleInit, control);
        subscribe(handleUnBroadcast, ubp);
        subscribe(handleFlp2pDeliver, flp2p);
    }
    private Address self;
    private HashSet<Address> allNodes = null;
    
    /*handlers*/
    Handler<SUBInit> handleInit = new Handler<SUBInit>() {

        @Override
        public void handle(SUBInit e) {
            self = e.getTopology().getSelfAddress();
            allNodes = new HashSet<Address>(e.getTopology().getAllAddresses());
        }
    };
    
    Handler<UnBroadcast> handleUnBroadcast = new Handler<UnBroadcast>() {

        @Override
        public void handle(UnBroadcast e) {
            Iterator<Address> iterNodes = allNodes.iterator();
            DataMessage toSend = e.getDataMsg();
            while (iterNodes.hasNext()) {
                Address address = iterNodes.next();
                trigger(new Flp2pSend(address, toSend),flp2p);
            }
        }
    };
    
    Handler<DataMessage> handleFlp2pDeliver = new Handler<DataMessage>() {

        @Override
        public void handle(DataMessage e) {
            logger.info("unreliable broadcast received a msg");
            trigger(new UnDeliver(e), ubp);
        }
    };
    
}
