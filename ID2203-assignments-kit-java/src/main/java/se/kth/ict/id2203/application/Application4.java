package se.kth.ict.id2203.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
    private int currWaitDelay;
    private Set<Address> neighborSet;
    private Address self;
    private Integer[] decisions;
    private Set<Integer> previousProposal;
    private Set<Integer> currentProposal;

    /**
     * Instantiates a new application0.
     */
    public Application4() {
        subscribe(handleInit, control);
        subscribe(handleStart, control);
        subscribe(handleContinue, timer);
        subscribe(handleConsoleInput, con);
        subscribe(handleUcDecide, uc);
    }
    
    Handler<Application4Init> handleInit = new Handler<Application4Init>() {
        public void handle(Application4Init event) {
            neighborSet = event.getNeighborSet();
            previousProposal = new HashSet<Integer>();
            currentProposal = new HashSet<Integer>();
            decisions = new Integer[event.getMaxInstance()];
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
            int id = e.getId();
            int val = e.getVal();
            logger.info("received a decision from paxos: "+id);
            previousProposal.remove(id);
            currentProposal.remove(id);
            decisions[id] = val;
            if(blocking){
                doDelay();
            }else{
                doNextCommand();
            }
        }
    };

    private final void doNextCommand() {
        while (!blocking && !commands.isEmpty()) {
            doCommand(commands.remove(0));
        }
    }

    private void doCommand(String cmd) {
        if (cmd.startsWith("D")) {
            currWaitDelay = Integer.parseInt(cmd.substring(1));
            previousProposal = new HashSet<Integer>(currentProposal);
            doDelay();
        }else if (cmd.startsWith("P")) {
            doPropose(Integer.parseInt(cmd.substring(1)),Integer.parseInt(cmd.substring(3)));
        } else if (cmd.startsWith("W")) {
            doWrite();
        } else if (cmd.startsWith("X")) {
            doShutdown();
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
    
    private void doDelay(){
        if(!previousProposal.isEmpty()){
            blocking = true;
        }else{
            doSleep(currWaitDelay);
        }
    }

    private void doShutdown() {
        System.out.println("2DIE");
        System.out.close();
        System.err.close();
        Kompics.shutdown();
        blocking = true;
    }

    
    private void doPropose(Integer id, Integer val) {
        logger.info("Process " + self.getId() + " proposed value " + val);
        currentProposal.add(id);
        trigger(new UcPropose(id, val), uc);
    }   
    
    
    private void doRead() {
        logger.info("Process " + self.getId() + " reading...");
        trigger(new ReadRequest(0), uc);
        blocking = true;
    }

    private void doWrite() {
        logger.info("Displaying results...");
        for (int i = 0; i < decisions.length; i++) {
            Integer integer = decisions[i];
            logger.info("paxos id: " + i + " ===> decides: "+ integer);
        }
    }
}
