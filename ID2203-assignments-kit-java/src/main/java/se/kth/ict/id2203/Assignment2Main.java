/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203;

import java.util.Set;
import org.apache.log4j.PropertyConfigurator;
import se.kth.ict.id2203.application.Application1a;
import se.kth.ict.id2203.application.Application1aInit;
import se.kth.ict.id2203.application.Application1b;
import se.kth.ict.id2203.application.Application1bInit;
import se.kth.ict.id2203.application.Application2;
import se.kth.ict.id2203.application.Application2Init;
import se.kth.ict.id2203.console.Console;
import se.kth.ict.id2203.console.java.JavaConsole;
import se.kth.ict.id2203.epfdp.EventuallyPerfectFailureDetector;
import se.kth.ict.id2203.epfdp.epfd.EPFD;
import se.kth.ict.id2203.epfdp.epfd.EPFDinit;
import se.kth.ict.id2203.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.flp2p.delay.DelayDropLink;
import se.kth.ict.id2203.flp2p.delay.DelayDropLinkInit;
import se.kth.ict.id2203.lpbport.ProbabilisticBroadcast;
import se.kth.ict.id2203.lpbport.lpb.LazyPb;
import se.kth.ict.id2203.lpbport.lpb.LazyPbInit;
import se.kth.ict.id2203.pfdp.pfd.PFD;
import se.kth.ict.id2203.pfdp.pfd.PfdInit;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.delay.DelayLink;
import se.kth.ict.id2203.pp2p.delay.DelayLinkInit;
import se.kth.ict.id2203.unbport.UnreliableBroadcast;
import se.kth.ict.id2203.unbport.unb.SUB;
import se.kth.ict.id2203.unbport.unb.SUBInit;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Fault;
import se.sics.kompics.Handler;
import se.sics.kompics.Kompics;
import se.sics.kompics.address.Address;
import se.sics.kompics.launch.Topology;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.mina.MinaNetwork;
import se.sics.kompics.network.mina.MinaNetworkInit;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.timer.java.JavaTimer;

/**
 *
 * @author fingolfin
 */
public class Assignment2Main extends ComponentDefinition {
    static {
        PropertyConfigurator.configureAndWatch("log4j.properties");
    }
    private static int selfId;
    private static String commandScript;
    Topology topology = Topology.load(System.getProperty("topology"), selfId);

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        selfId = Integer.parseInt(args[0]);
        commandScript = args[1];

        Kompics.createAndStart(Assignment2Main.class);
    }

    /**
     * Instantiates a new assignment0 group0.
     */
    public Assignment2Main() {
        // create components
        Component time = create(JavaTimer.class);
        Component network = create(MinaNetwork.class);
        Component con = create(JavaConsole.class);
        Component flp2p = create(DelayDropLink.class);
        Component app = create(Application2.class);
        Component lpb = create(LazyPb.class);
        Component sub = create(SUB.class); 

        // handle possible faults in the components
        subscribe(handleFault, time.control());
        subscribe(handleFault, network.control());
        subscribe(handleFault, con.control());
        subscribe(handleFault, flp2p.control());
        subscribe(handleFault, app.control());
        subscribe(handleFault, lpb.control());
        subscribe(handleFault, sub.control());

        // initialize the components
        Address self = topology.getSelfAddress();
        Set<Address> neighborSet = topology.getNeighbors(self);

        trigger(new MinaNetworkInit(self, 5), network.control());
        trigger(new DelayDropLinkInit(topology, 42), flp2p.control());
        trigger(new Application2Init(commandScript, neighborSet, self), app.control());
        trigger(new SUBInit(topology), sub.control());
        trigger(new LazyPbInit(topology, 3, 0.6, 4, 3000), lpb.control());

        // connect the components


        connect(app.required(ProbabilisticBroadcast.class),
                lpb.provided(ProbabilisticBroadcast.class));
        connect(app.required(Timer.class), time.provided(Timer.class));
        connect(app.required(Console.class),
                con.provided(Console.class));
        
        connect(lpb.required(Timer.class), time.provided(Timer.class));
        connect(lpb.required(UnreliableBroadcast.class),
                sub.provided(UnreliableBroadcast.class));
        connect(lpb.required(FairLossPointToPointLink.class),
                flp2p.provided(FairLossPointToPointLink.class));

        connect(sub.required(FairLossPointToPointLink.class),
                flp2p.provided(FairLossPointToPointLink.class));
       
        connect(flp2p.required(Timer.class), time.provided(Timer.class));
        connect(flp2p.required(Network.class), network.provided(Network.class));


    }
    Handler<Fault> handleFault = new Handler<Fault>() {
        public void handle(Fault fault) {
            fault.getFault().printStackTrace(System.err);
        }
    };
}
