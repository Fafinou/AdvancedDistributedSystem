package se.kth.ict.id2203.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.console.Console;
import se.kth.ict.id2203.console.ConsoleLine;
import se.kth.ict.id2203.riwcport.AtomicRegister;
import se.kth.ict.id2203.riwcport.ReadRequest;
import se.kth.ict.id2203.riwcport.ReadResponse;
import se.kth.ict.id2203.riwcport.WriteRequest;
import se.kth.ict.id2203.riwcport.WriteResponse;
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
public class Application3a extends ComponentDefinition{
     Positive<Timer> timer = requires(Timer.class);
    Positive<AtomicRegister> ar = requires(AtomicRegister.class);
    Positive<Console> con = requires(Console.class);
    private static final Logger logger =
            LoggerFactory.getLogger(Application3a.class);
    private List<String> commands;
    private boolean blocking;
    private Set<Address> neighborSet;
    private Address self;

    /**
     * Instantiates a new application0.
     */
    public Application3a() {
        subscribe(handleInit, control);
        subscribe(handleStart, control);
        subscribe(handleContinue, timer);
        subscribe(handleConsoleInput, con);
        subscribe(handleReadResponse, ar);
        subscribe(handleWriteResponse, ar);
        
        
    }
    Handler<Application3aInit> handleInit = new Handler<Application3aInit>() {
        public void handle(Application3aInit event) {
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
    
    Handler<WriteResponse> handleWriteResponse = new Handler<WriteResponse>() {

        @Override
        public void handle(WriteResponse e) {
            logger.info("Writing completed");
            blocking = false;
        }
    };
    
    Handler<ReadResponse> handleReadResponse = new Handler<ReadResponse>() {

        @Override
        public void handle(ReadResponse e) {
            logger.info("Read value: " + e.getVal());
            blocking = false;
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
        }else if(cmd.startsWith("W")){
            doWrite(Integer.parseInt(cmd.substring(1)));
        } else if (cmd.startsWith("X")) {
            doShutdown();
        }else if (cmd.startsWith("R")){
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

    private void doWrite(int parseInt) {
        logger.info("Trying to write the message "+ parseInt);
        trigger(new WriteRequest(parseInt), ar);
        blocking = true;
    }

    private void doRead() {
        logger.info("Trying to read...");
        trigger(new ReadRequest(), ar);
        blocking = true;
    }
}
