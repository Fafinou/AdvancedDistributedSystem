/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203;

import se.sics.kompics.launch.Scenario;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author fingolfin
 */
public class Assignment2Executor {
       /**
     * The main method.
     *
     * @param args the arguments
     */
    public static final void main(String[] args) {

        Topology topology2 = new Topology() {
            {
                node(1, "127.0.0.1", 22030);
                node(2, "127.0.0.1", 22031);
                node(3, "127.0.0.1", 22032);
                node(4, "127.0.0.1", 22033);
                node(5, "127.0.0.1", 22034);
                //link(4, 2, 1500, 0.5);
                defaultLinks(1000, 0);
            }
        };

        Topology topology1 = new Topology() {
            {
                node(1, "127.0.0.1", 22031);
                node(2, "127.0.0.1", 22032);
                node(3, "127.0.0.1", 22033);
                link(1, 2, 3000, 0).bidirectional();
                link(2, 3, 3000, 0);
                defaultLinks(1000, 0);
            }
        };


        Scenario scenarioPfd = new Scenario(Assignment1aMain.class) {
            {
                command(1, "S500:S2000:X");
                command(2, "S500:S4000:S5000:X");
                command(3, "S2500:X");
                command(4, "X");
                command(5,"S60000");
            }
        };

        Scenario scenario2 = new Scenario(Assignment1aMain.class) {
            {
                command(1, "S2500:La1:S300:PA1:X").recover("S400:Pff", 1000);
                command(2, "S2500:Pb2:S300:LB2");
                command(3, "S2500:Lc3:S300:PC3");
                command(4, "S2500:Pd4:S300:LD4");
            }
        };

        
        Scenario scenarioEpfd = new Scenario(Assignment1bMain.class) {
            {
                command(1, "S40000:X");
                command(2, "S18000:X");
                command(3, "S10000:X").recover("R:S42000:X", 4000);
                command(4, "S4000:X");
                command(5,"S120000");
            }
        };

        //scenarioPfd.executeOn(topology2);
        scenarioEpfd.executeOn(topology2);
        
        //scenario1.executeOn(topology2);
        // scenario2.executeOn(topology1);
        // scenario2.executeOn(topology2);
        // scenario1.executeOnFullyConnected(topology1);
        // scenario1.executeOnFullyConnected(topology2);
        // scenario2.executeOnFullyConnected(topology1);
        // scenario2.executeOnFullyConnected(topology2);

        System.exit(0);
        // move one of the below scenario executions above the exit for
        // execution

    }
}
