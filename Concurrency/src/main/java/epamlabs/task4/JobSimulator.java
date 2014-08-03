package epamlabs.task4;

import java.util.Random;

/**
 * Created by Adevi on 8/1/2014.
 */
public class JobSimulator implements Runnable {
    private static final long MAX_WORKING_TIME = 66;

    // In this case static Random object is OK, because it is NOT involved in concurrency
    private static Random random = new Random();

    private Integer number;

    public JobSimulator(Integer number) {
        this.number = number;
    }

    @Override
    public void run() {
        System.out.println("Hello, I am a thread №" + number);
        try {
            // Simulating random amount of work
            Thread.sleep(Math.abs(random.nextLong() % MAX_WORKING_TIME));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
}
