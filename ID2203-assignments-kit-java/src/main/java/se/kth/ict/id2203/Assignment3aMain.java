package se.kth.ict.id2203;

import java.util.Set;
import org.apache.log4j.PropertyConfigurator;
import se.kth.ict.id2203.application.Application1a;
import se.kth.ict.id2203.application.Application1aInit;
import se.kth.ict.id2203.application.Application1b;
import se.kth.ict.id2203.application.Application1bInit;
import se.kth.ict.id2203.application.Application2;
import se.kth.ict.id2203.application.Application2Init;
import se.kth.ict.id2203.application.Application3a;
import se.kth.ict.id2203.application.Application3aInit;
import se.kth.ict.id2203.bebport.BEB;
import se.kth.ict.id2203.bebport.beb.BasicBcast;
import se.kth.ict.id2203.bebport.beb.BasicBcastInit;
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
import se.kth.ict.id2203.pfdp.PerfectFailureDetector;
import se.kth.ict.id2203.pfdp.pfd.PFD;
import se.kth.ict.id2203.pfdp.pfd.PfdInit;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.delay.DelayLink;
import se.kth.ict.id2203.pp2p.delay.DelayLinkInit;
import se.kth.ict.id2203.riwcport.AtomicRegister;
import se.kth.ict.id2203.riwcport.riwc.RIWC;
import se.kth.ict.id2203.riwcport.riwc.RIWCInit;
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
 * @author ALEX
 */
public class Assignment3aMain extends ComponentDefinition {
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

        Kompics.createAndStart(Assignment3aMain.class);
    }

    /**
     * Instantiates a new assignment0 group0.
     */
    public Assignment3aMain() {
    // create components
        Component time = create(JavaTimer.class);
        Component network = create(MinaNetwork.class);
        Component con = create(JavaConsole.class);
        Component pfd = create(PFD.class);
        Component pp2p = create(DelayLink.class);
        Component riwc = create(RIWC.class);
        Component app = create(Application3a.class);    
        Component beb = create(BasicBcast.class); 

        // handle possible faults in the components
        subscribe(handleFault, time.control());
        subscribe(handleFault, network.control());
        subscribe(handleFault, con.control());
        subscribe(handleFault, pfd.control());
        subscribe(handleFault, pp2p.control());
        subscribe(handleFault, riwc.control());
        subscribe(handleFault, app.control());
        subscribe(handleFault, beb.control());
   
    
     Address self = topology.getSelfAddress();
        Set<Address> neighborSet = topology.getNeighbors(self);
    // initialize the components
        trigger(new MinaNetworkInit(self, 5), network.control());
        trigger(new DelayLinkInit(topology), pp2p.control());
        trigger(new Application3aInit(commandScript, neighborSet, self), app
                .control());
        trigger(new PfdInit(topology, 1000, 1001),
                pfd.control());
        trigger(new RIWCInit(topology), riwc.control());
        trigger(new BasicBcastInit(topology), beb.control());
    
    // connect the components
        connect(app.required(AtomicRegister.class), riwc.provided(AtomicRegister.class));
        connect(app.required(Timer.class), time.provided(Timer.class));
        connect(riwc.required(BEB.class), beb.provided(BEB.class));
        connect(riwc.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));
        connect(riwc.required(PerfectFailureDetector.class), pfd.provided(PerfectFailureDetector.class));
        connect(beb.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));
        connect(pfd.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));
        connect(pfd.required(Timer.class), time.provided(Timer.class));
        connect(pp2p.required(Timer.class), time.provided(Timer.class));
        connect(pp2p.required(Network.class), network.provided(Network.class));
    }
    
    Handler<Fault> handleFault = new Handler<Fault>() {
        public void handle(Fault fault) {
            fault.getFault().printStackTrace(System.err);
        }
    };
}
