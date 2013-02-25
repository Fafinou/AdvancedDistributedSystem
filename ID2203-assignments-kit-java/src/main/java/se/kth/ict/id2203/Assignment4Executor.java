package se.kth.ict.id2203;

import se.sics.kompics.launch.Scenario;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author ALEX & fingolfin
 */
public class Assignment4Executor {
    
    public static final void main(String[] args) {

        Topology topologyEx1 = new Topology() {
            {
                node(1, "127.0.0.1", 22030);
                node(2, "127.0.0.1", 22031);
                node(3, "127.0.0.1", 22032);

                defaultLinks(1000, 0);
            }
        };

        Topology topologyEx2 = new Topology() {
            {
                node(1, "127.0.0.1", 22031);
                node(2, "127.0.0.1", 22032);
                node(3, "127.0.0.1", 22033);
                link(1, 2, 1000, 0).bidirectional();
                link(1, 3, 2000, 0).bidirectional();
                link(2, 3, 1750, 0).bidirectional();
            }
        };

        Topology topologyEx4 = new Topology() {
            {
                node(1, "127.0.0.1", 22031);
                node(2, "127.0.0.1", 22032);
                node(3, "127.0.0.1", 22033);
                link(1, 2, 2000, 0).bidirectional();
                link(1, 3, 2000, 0).bidirectional();
                link(2, 3, 1750, 0).bidirectional();
            }
        };

        Scenario scenarioEx1a = new Scenario(Assignment3aMain.class) {
            {
                command(1, "D30000");
                command(2, "D500:W4:D25000");
                command(3, "D10000:R");
            }
        };

        Scenario scenarioEx1b = new Scenario(Assignment3bMain.class) {
            {
                command(1, "D30000");
                command(2, "D500:W4:D25000");
                command(3, "D10000:R");
            }
        };

        Scenario scenarioEx2 = new Scenario(Assignment3bMain.class) {
            {
                command(1, "D500:W5:R:D5000:R:D30000");
                command(2, "D500:W6:R:D5000:R:D30000");
                command(3, "D500:R:D500:R:D10000", 20000);
            }
        };




        Scenario scenario123 = new Scenario(Assignment3bMain.class) {
            {
                command(1, "D500:W1:R:D500:R:D800");
                command(2, "D500:W2:R:D500:R:D800", 100);
                command(3, "D500:W3:R:D500:R:D800", 200);
            }
        };


        Scenario scenario132 = new Scenario(Assignment3bMain.class) {
            {
                command(1, "D500:W1:R:D500:R:D800");
                command(3, "D10000:W3:R:D500:R:D800", 100);
                command(2, "D10000:W2:R:D500:R:D800", 200);
            }
        };


        Scenario scenario213 = new Scenario(Assignment3bMain.class) {
            {
                command(2, "D500:W2:R:D500:R:D800");
                command(1, "D500:W1:R:D500:R:D800", 100);
                command(3, "D500:W3:R:D500:R:D800", 200);
            }
        };


        Scenario scenario231 = new Scenario(Assignment3bMain.class) {
            {
                command(2, "D500:W2:R:D500:R:D800");
                command(3, "D500:W3:R:D500:R:D800", 100);
                command(1, "D500:W1:R:D500:R:D800", 200);
            }
        };

        Scenario scenario312 = new Scenario(Assignment3bMain.class) {
            {
                command(3, "D500:W3:R:D500:R:D800");
                command(1, "D500:W1:R:D500:R:D800", 100);
                command(2, "D500:W2:R:D500:R:D800", 200);
            }
        };

        Scenario scenario321 = new Scenario(Assignment3bMain.class) {
            {
                command(3, "D500:W3:R:D500:R:D800");
                command(2, "D500:W2:R:D500:R:D800", 100);
                command(1, "D500:W1:R:D500:R:D800", 200);
            }
        };

            //scenarioEx1a.executeOn(topologyEx12);
            //scenarioEx1b.executeOn(topologyEx12);
            //scenarioEx2.executeOn(topologyEx12);
            
            scenario123.executeOn(topologyEx3);
            //scenario132.executeOn(topologyEx3);
            //scenario213.executeOn(topologyEx3);
            //scenario231.executeOn(topologyEx3);
            scenario312.executeOn(topologyEx3);
            //scenario321.executeOn(topologyEx3);
            
            //scenario123.executeOn(topologyEx4);
            //scenario132.executeOn(topologyEx4);
            //scenario213.executeOn(topologyEx4);
            //scenario231.executeOn(topologyEx4);
            //scenario312.executeOn(topologyEx4);
            //scenario321.executeOn(topologyEx4);

        System.exit(0);
        // move one of the below scenario executions above the exit for
        // execution

    }
}
