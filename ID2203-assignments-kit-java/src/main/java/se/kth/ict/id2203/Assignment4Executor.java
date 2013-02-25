package se.kth.ict.id2203;

import se.sics.kompics.launch.Scenario;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author ALEX & fingolfin
 */
public class Assignment4Executor {

    public static final void main(String[] args) {

        Topology topologyEx3 = new Topology() {
            {
                node(1, "127.0.0.1", 22030);
                //node(2, "127.0.0.1", 22031);
                //node(3, "127.0.0.1", 22032);

                defaultLinks(1000, 0);
            }
        };



        Scenario scenarioEx3a = new Scenario(Assignment4Main.class) {
            {
                command(1, "D2200:P1-1");
                command(2, "D2000:P1-2");
                command(3, "D2000:P1-3");
            }
        };

        Scenario scenarioEx3b = new Scenario(Assignment4Main.class) {
            {
                command(1, "D2000:P1-1");
                command(2, "D2200:P1-2");
                command(3, "D2200:P1-3");
            }
        };

        Scenario scenarioEx1 = new Scenario(Assignment4Main.class) {
            {
                command(1, "P1-7:D100:P3-5:P4-9:D20000:W");
            }
        };
        scenarioEx1.executeOn(topologyEx3);
        //scenarioEx3b.executeOn(topologyEx3);
        //scenarioEx4.executeOn(topologyEx4);



        System.exit(0);
        // move one of the below scenario executions above the exit for
        // execution

    }
}
