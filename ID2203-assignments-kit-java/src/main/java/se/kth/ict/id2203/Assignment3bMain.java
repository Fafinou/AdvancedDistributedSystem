package se.kth.ict.id2203;

import java.util.Set;
import org.apache.log4j.PropertyConfigurator;
import se.kth.ict.id2203.application.Application3b;
import se.kth.ict.id2203.application.Application3bInit;
import se.kth.ict.id2203.bebport.BEB;
import se.kth.ict.id2203.bebport.beb.BasicBcast;
import se.kth.ict.id2203.bebport.beb.BasicBcastInit;
import se.kth.ict.id2203.console.Console;
import se.kth.ict.id2203.console.java.JavaConsole;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.delay.DelayLink;
import se.kth.ict.id2203.pp2p.delay.DelayLinkInit;
import se.kth.ict.id2203.riwcport.AtomicRegister;
import se.kth.ict.id2203.riwcport.riwcm.RIWCM;
import se.kth.ict.id2203.riwcport.riwcm.RIWCMInit;
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
 * @author ALEX & fingolfin
 */
public class Assignment3bMain extends ComponentDefinition {
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

        Kompics.createAndStart(Assignment3bMain.class);
    }

    /**
     * Instantiates a new assignment0 group0.
     */
    public Assignment3bMain() {
    // create components
        Component time = create(JavaTimer.class);
        Component network = create(MinaNetwork.class);
        Component con = create(JavaConsole.class);
        Component pp2p = create(DelayLink.class);
        Component riwcm = create(RIWCM.class);
        Component app = create(Application3b.class);    
        Component beb = create(BasicBcast.class); 

        // handle possible faults in the components
        subscribe(handleFault, time.control());
        subscribe(handleFault, network.control());
        subscribe(handleFault, con.control());
        subscribe(handleFault, pp2p.control());
        subscribe(handleFault, riwcm.control());
        subscribe(handleFault, app.control());
        subscribe(handleFault, beb.control());
   
    
     Address self = topology.getSelfAddress();
        Set<Address> neighborSet = topology.getNeighbors(self);
    // initialize the components
        trigger(new MinaNetworkInit(self, 5), network.control());
        trigger(new DelayLinkInit(topology), pp2p.control());
        trigger(new Application3bInit(commandScript, neighborSet, self), app
                .control());
        trigger(new RIWCMInit(topology), riwcm.control());
        trigger(new BasicBcastInit(topology), beb.control());
    
    // connect the components
        connect(app.required(AtomicRegister.class), riwcm.provided(AtomicRegister.class));
        connect(app.required(Timer.class), time.provided(Timer.class));
        connect(app.required(Console.class), con.provided(Console.class));
        
        connect(riwcm.required(BEB.class), beb.provided(BEB.class));
        connect(riwcm.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));        
        
        connect(beb.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));
        
        
        connect(pp2p.required(Timer.class), time.provided(Timer.class));
        connect(pp2p.required(Network.class), network.provided(Network.class));
    }
    
    Handler<Fault> handleFault = new Handler<Fault>() {
        public void handle(Fault fault) {
            fault.getFault().printStackTrace(System.err);
        }
    };
}
