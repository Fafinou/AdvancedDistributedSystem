/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.console.Console;
import se.kth.ict.id2203.console.ConsoleLine;
import se.kth.ict.id2203.lpbport.PbBroadcast;
import se.kth.ict.id2203.lpbport.PbDeliver;
import se.kth.ict.id2203.lpbport.ProbabilisticBroadcast;
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
 * @author fingolfin & alegeo
 */
public class Application2 extends ComponentDefinition{
    Positive<Timer> timer = requires(Timer.class);
    Positive<ProbabilisticBroadcast> pb = requires(ProbabilisticBroadcast.class);
    Positive<Console> con = requires(Console.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application2.class);
    private List<String> commands;
    private boolean blocking;
    private Set<Address> neighborSet;
    private Address self;

    /**
     * Instantiates a new application0.
     */
    public Application2() {
        subscribe(handleInit, control);
        subscribe(handleStart, control);
        subscribe(handleContinue, timer);
        subscribe(handleConsoleInput, con);
        subscribe(handleDeliver, pb);
        
    }
    Handler<Application2Init> handleInit = new Handler<Application2Init>() {
        public void handle(Application2Init event) {
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
    
    Handler<PbDeliver> handleDeliver = new Handler<PbDeliver>() {

        @Override
        public void handle(PbDeliver e) {
            logger.info("Received a message "+e.getMsg()+" from: "+e.getSource());
        }
    };
    
    private final void doNextCommand() {
        while (!blocking && !commands.isEmpty()) {
            doCommand(commands.remove(0));
        }
    }
    
    private void doCommand(String cmd) {
        if (cmd.startsWith("S")) {
            doSleep(Integer.parseInt(cmd.substring(1)));
        } else if (cmd.startsWith("X")) {
            doShutdown();
        } else if (cmd.startsWith("R")){
            doRecovery();
        } else if (cmd.equals("help")) {
            doHelp();
        }else if (cmd.startsWith("B")){
            doBroadCast(cmd.substring(1));
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

    private void doRecovery() {
        logger.info("Node {} recovered from crash",self);
    }

    private void doBroadCast(String msg) {
        logger.info("broadcasting");
        trigger(new PbBroadcast(self, msg),pb);
    }
    
}
