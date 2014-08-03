package epamlabs.task4;

/**
 * @author Yurii Andrieiev
 * This class wraps the given runnable, executes it, than makes current thread waiting and
 * transfers execution no the next Scheduler
 */
public class Scheduler implements Runnable {
    private final Runnable job;
    private Scheduler next;
    private volatile Boolean wait;

    public Scheduler(Runnable job) {
        this(job, Boolean.TRUE);
    }

    public Scheduler(Runnable job, Boolean wait) {
        this.job = job;
        this.wait = wait;
    }

    public void setNext(Scheduler next) {
        this.next = next;
    }

    private synchronized void waitForMyTurn() throws InterruptedException {
        while (wait)
            wait();
    }

    private void passTheTurn() {
        wait = Boolean.TRUE;
        next.wait = Boolean.FALSE;
        synchronized (next) {
            next.notify();
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                waitForMyTurn();
            } catch (InterruptedException e) {
                break;
            }
            job.run();
            passTheTurn();
        }
    }
}