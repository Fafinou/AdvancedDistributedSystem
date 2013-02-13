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
import se.kth.ict.id2203.epfdp.EventuallyPerfectFailureDetector;
import se.kth.ict.id2203.epfdp.Restore;
import se.kth.ict.id2203.epfdp.Suspect;
import se.kth.ict.id2203.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.flp2p.Flp2pSend;
import se.kth.ict.id2203.pfdp.Crash;
import se.kth.ict.id2203.pfdp.PerfectFailureDetector;
import se.kth.ict.id2203.pfdp.pfd.HeartBeatMessage;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.Pp2pSend;
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
public class Application1b extends ComponentDefinition {
    
    Positive<Timer> timer = requires(Timer.class);
    Positive<EventuallyPerfectFailureDetector> epfd = requires(EventuallyPerfectFailureDetector.class);
    Positive<Console> con = requires(Console.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application1a.class);
    private List<String> commands;
    private boolean blocking;
    private Set<Address> neighborSet;
    private Address self;

    /**
     * Instantiates a new application0.
     */
    public Application1b() {
        subscribe(handleInit, control);
        subscribe(handleStart, control);
        subscribe(handleContinue, timer);
        subscribe(handleConsoleInput, con);
        subscribe(handleRestore, epfd);
        subscribe(handleSuspected, epfd);
        //subscribe(handleHeartBeat, pp2p);
        
    }
    Handler<Application1bInit> handleInit = new Handler<Application1bInit>() {
        public void handle(Application1bInit event) {
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

    Handler<Suspect> handleSuspected = new Handler<Suspect>() {

        @Override
        public void handle(Suspect e) {
            logger.info("Suspected node {} to have crashed. Period => {} ms", 
                        e.getSuspectedProcess(),
                        e.getPeriod());
        }
    };
    
    Handler<Restore> handleRestore = new Handler<Restore>() {

        @Override
        public void handle(Restore e) {
            logger.info("Restored node {}. Period => {} ms", 
                    e.getRestoredProcess(),
                    e.getPeriod());
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
    
}
