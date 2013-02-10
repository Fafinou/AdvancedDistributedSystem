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
import se.kth.ict.id2203.console.Console;
import se.kth.ict.id2203.console.java.JavaConsole;
import se.kth.ict.id2203.epfdp.EventuallyPerfectFailureDetector;
import se.kth.ict.id2203.epfdp.epfd.EPFD;
import se.kth.ict.id2203.epfdp.epfd.EPFDinit;
import se.kth.ict.id2203.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.flp2p.delay.DelayDropLink;
import se.kth.ict.id2203.flp2p.delay.DelayDropLinkInit;
import se.kth.ict.id2203.pfdp.pfd.PFD;
import se.kth.ict.id2203.pfdp.pfd.PfdInit;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.delay.DelayLink;
import se.kth.ict.id2203.pp2p.delay.DelayLinkInit;
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
public class Assignment1bMain extends ComponentDefinition{
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

        Kompics.createAndStart(Assignment1bMain.class);
    }

    /**
     * Instantiates a new assignment0 group0.
     */
    public Assignment1bMain() {
        // create components
        Component time = create(JavaTimer.class);
        Component network = create(MinaNetwork.class);
        Component con = create(JavaConsole.class);
        Component pp2p = create(DelayLink.class);
        Component app = create(Application1b.class);
        Component epfd = create(EPFD.class);

        // handle possible faults in the components
        subscribe(handleFault, time.control());
        subscribe(handleFault, network.control());
        subscribe(handleFault, con.control());
        subscribe(handleFault, pp2p.control());
        subscribe(handleFault, app.control());
        subscribe(handleFault, epfd.control());

        // initialize the components
        Address self = topology.getSelfAddress();
        Set<Address> neighborSet = topology.getNeighbors(self);

        trigger(new MinaNetworkInit(self, 5), network.control());
        trigger(new DelayLinkInit(topology), pp2p.control());
        trigger(new Application1bInit(commandScript, neighborSet, self), app
                .control());
        trigger(new EPFDinit(topology, 1000, 1000, false),
                epfd.control());

        // connect the components


        connect(epfd.required(PerfectPointToPointLink.class),
                pp2p.provided(PerfectPointToPointLink.class));
        connect(epfd.required(Timer.class), time.provided(Timer.class));


        connect(app.required(Console.class),
                con.provided(Console.class));
       
        connect(app.required(Timer.class), time.provided(Timer.class));
        connect(app.required(EventuallyPerfectFailureDetector.class),
                epfd.provided(EventuallyPerfectFailureDetector.class));
        connect(pp2p.required(Timer.class), time.provided(Timer.class));
        connect(pp2p.required(Network.class), network.provided(Network.class));


    }
    Handler<Fault> handleFault = new Handler<Fault>() {
        public void handle(Fault fault) {
            fault.getFault().printStackTrace(System.err);
        }
    };
}
