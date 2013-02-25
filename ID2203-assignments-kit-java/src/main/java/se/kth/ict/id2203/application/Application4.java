package se.kth.ict.id2203.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.console.Console;
import se.kth.ict.id2203.console.ConsoleLine;
import se.kth.ict.id2203.riwcport.ReadRequest;
import se.kth.ict.id2203.riwcport.WriteRequest;
import se.kth.ict.id2203.ucp.UcDecide;
import se.kth.ict.id2203.ucp.UcPropose;
import se.kth.ict.id2203.ucp.UniformConsensus;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Kompics;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.address.Address;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author ALEX & fingolfin
 */
public class Application4 extends ComponentDefinition {

    Positive<Timer> timer = requires(Timer.class);
    Positive<UniformConsensus> uc = requires(UniformConsensus.class);
    Positive<Console> con = requires(Console.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application3a.class);
    private List<String> commands;
    private boolean blocking;
    private int i;
    private int j;
    private Set<Address> neighborSet;
    private Address self;

    /**
     * Instantiates a new application0.
     */
    public Application4() {
        subscribe(handleInit, control);
        subscribe(handleStart, control);
        subscribe(handleContinue, timer);
        subscribe(handleConsoleInput, con);
        subscribe(handleUcDecide, uc);
        subscribe(handleUcPropose, uc);
    }
    
    Handler<Application4Init> handleInit = new Handler<Application4Init>() {
        public void handle(Application4Init event) {
            neighborSet = event.getNeighborSet();
            self = event.getSelf();
            commands = new ArrayList<String>(Arrays.asList(event.getCommandScript().split(":")));
            commands.add("$DONE");
            blocking = false;
        }
    };
    Handler<Start> handleStart = new Handler<Start>() {
        public void handle(Start event) {
            doNextCommand();
        }
    };
    Handler<ApplicationContinue> handleContinue = new Handler<ApplicationContinue>() {
        public void handle(ApplicationContinue event) {
            logger.info("Woke up from sleep");
            blocking = false;
            doNextCommand();
        }
    };
    Handler<ConsoleLine> handleConsoleInput = new Handler<ConsoleLine>() {
        @Override
        public void handle(ConsoleLine event) {
            commands.addAll(Arrays.asList(event.getLine().trim().split(":")));
            doNextCommand();
        }
    };
    Handler<UcDecide> handleUcDecide = new Handler<UcDecide>() {
        @Override
        public void handle(UcDecide e) {
//            logger.info("Writing completed on register " + e.getReg());
//            blocking = false;
//            doNextCommand();
        }
    };
    Handler<UcPropose> handleUcPropose = new Handler<UcPropose>() {
        @Override
        public void handle(UcPropose e) {
//            logger.info("Read value: " + e.getVal()+" On register "+e.getReg());
//            blocking = false;
//            doNextCommand();
        }
    };

    private final void doNextCommand() {
        while (!blocking && !commands.isEmpty()) {
            doCommand(commands.remove(0));
        }
    }

    private void doCommand(String cmd) {
        if (cmd.startsWith("D")) {
            doSleep(Integer.parseInt(cmd.substring(1)));
        }else if (cmd.startsWith("P")) {
            doPropose(Integer.parseInt(cmd.substring(1)));
        }else if (cmd.startsWith("Dk")) {
            doDk(Integer.parseInt(cmd.substring(1)));
        } else if (cmd.startsWith("W")) {
            doWrite(Integer.parseInt(cmd.substring(1)));
        } else if (cmd.startsWith("X")) {
            doShutdown();
        } else if (cmd.startsWith("R")) {
            doRead();
        } else if (cmd.equals("help")) {
            doHelp();
        } else if (cmd.equals("$DONE")) {
            logger.info("DONE ALL OPERATIONS");
        } else {
            logger.info("Bad command: '{}'. Try 'help'", cmd);
        }
    }

    private final void doHelp() {
        logger.info("Available commands: P<m>, L<m>, S<n>, help, X");
        logger.info("Pm: sends perfect message 'm' to all neighbors");
        logger.info("Lm: sends lossy message 'm' to all neighbors");
        logger.info("Sn: sleeps 'n' milliseconds before the next command");
        logger.info("help: shows this help message");
        logger.info("X: terminates this process");
    }

    private void doSleep(long delay) {
        logger.info("Sleeping {} milliseconds...", delay);

        ScheduleTimeout st = new ScheduleTimeout(delay);
        st.setTimeoutEvent(new ApplicationContinue(st));
        trigger(st, timer);

        blocking = true;
    }

    private void doShutdown() {
        System.out.println("2DIE");
        System.out.close();
        System.err.close();
        Kompics.shutdown();
        blocking = true;
    }

    
    private void doPropose(int parseInt) {
        logger.info("Process " + self.getId() + " Trying to write the message " + parseInt);
        trigger(new WriteRequest(0, parseInt), uc);
        blocking = true;
    }
    
    private void doDk(int parseInt) {
        logger.info("Process " + self.getId() + " proposed value " + parseInt);
        trigger(new UcPropose(0, parseInt), uc);
        blocking = true;
    }
    
    
    private void doWrite(int parseInt) {
        logger.info("Process " + self.getId() + " Trying to write the message " + parseInt);
        trigger(new WriteRequest(0, parseInt), uc);
        blocking = true;
    }

    private void doRead() {
        logger.info("Process " + self.getId() + " reading...");
        trigger(new ReadRequest(0), uc);
        blocking = true;
    }
}
