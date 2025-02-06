package testSuite.in_progress.searching_state_space.timertask_cannot_reschedule;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * URLConnection
 * 
 * ->  STATE_OPENED -> STATE_SETTER -> STATE_CONNECTED
 */
public class Test {

    /*
     * Error cannot reschedule  a timer
     */
     public static void example1801324( Map<String, Timer> timers, String sessionKey) {

        // Step 1) Get the timer
        Timer timer = timers.get(sessionKey);

        // Step 2) Cancel the timer
        timer.cancel();

        // Step 3) Schedule a new task for this timer -> ERROR Cannot reschedule a Timer
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timer task completed.");
            }
        }, 1000);
    }
}
