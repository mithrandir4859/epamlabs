package epamlabs.task4;

/**
 * Created by Adevi on 8/1/2014.
 */
public class Demo {
    private static final int THREADS_NUMBER = 16;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Consecutive threads, task num. 4");
        Scheduler[] schedulers = new Scheduler[THREADS_NUMBER];
        {
            int i = 0;
            schedulers[i] = new Scheduler(new JobSimulator(i), Boolean.FALSE);
            i++;
            for (; i < THREADS_NUMBER; i++) {
                schedulers[i] = new Scheduler(new JobSimulator(i));
            }
            schedulers[THREADS_NUMBER - 1].setNext(schedulers[0]);
        }
        for (int i = 0; i < THREADS_NUMBER - 1; i++) {
            schedulers[i].setNext(schedulers[i+1]);
        }

        Thread[] threads = new Thread[THREADS_NUMBER];
        for (int i = 0; i < THREADS_NUMBER; i++) {
            threads[i] = new Thread(schedulers[i]);
        }
        for (Thread thread: threads)
            thread.start();

        Thread.sleep(3_500);
        for (Thread thread: threads)
            thread.interrupt();
    }
}
