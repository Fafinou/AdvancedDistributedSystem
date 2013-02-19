package se.kth.ict.id2203;

import org.apache.log4j.PropertyConfigurator;
import se.kth.ict.id2203.console.java.JavaConsole;
import se.kth.ict.id2203.pp2p.delay.DelayLink;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Fault;
import se.sics.kompics.Handler;
import se.sics.kompics.Kompics;
import se.sics.kompics.launch.Topology;
import se.sics.kompics.network.mina.MinaNetwork;
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
        //Component riwcm = create(RIWCM.class);
        //Component app = create(Application3b.class);    
        //Component beb = create(BEB.class); 

        // handle possible faults in the components
        subscribe(handleFault, time.control());
        subscribe(handleFault, network.control());
        subscribe(handleFault, con.control());
        subscribe(handleFault, pp2p.control());
        //subscribe(handleFault, riwcm.control());
        //subscribe(handleFault, app.control());
        //subscribe(handleFault, beb.control());
   }
    
    
    // initialize the components
    //TODO
    
    // connect the components
    //TODO
    
    Handler<Fault> handleFault = new Handler<Fault>() {
        public void handle(Fault fault) {
            fault.getFault().printStackTrace(System.err);
        }
    };
}
