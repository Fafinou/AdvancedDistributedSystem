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
                for (int i = 1; i <= 10; i++) {
                    node(i, "127.0.0.1",22050+i);
                }
                defaultLinks(1500, 0.3);
            }
        };


        Scenario scenario1 = new Scenario(Assignment2Main.class) {
            {
                for (int i = 1; i <= 10; i++) {
                    command(i, "Bmsgfrom"+i+":S1500:Bother_msg_from"+i);
                    
                }
                
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

        

        //scenarioPfd.executeOn(topology2);
        scenario1.executeOn(topology2);
        
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
