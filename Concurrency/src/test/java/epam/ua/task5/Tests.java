package epam.ua.task5;

import epam.ua.task5.impl.ThreadPoolImp;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Adevi on 8/2/2014.
 */
public class Tests {
    public static final int DEFAULT_TIMEOUT = 1000;
    ThreadPool threadPool = new ThreadPoolImp(10);

    @Test(timeout = DEFAULT_TIMEOUT)
    public void empty_pool_shutdowns_correctly() {
        threadPool.shutdown();
    }

    @Test
    public void waiting_point_works_correctly(){
        long sleepingTime = 1000;
        long time = System.nanoTime();
        threadPool.add(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        }).join();
        Assert.assertTrue(System.nanoTime() - time >= sleepingTime * 1_000_000);
    }

    @Test(timeout = 1000)
    public void works_ok_with_many_tasks(){
        for (int i = 0; i < 10_000; i++) {
            final int j = i;
            threadPool.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

            });
        }
        threadPool.shutdown();
    }

    @Test
    public void actually_finishes_tasks(){
        final int taskNum = 10_000;
        final boolean[] flags = new boolean[taskNum];
        final WaitingPoint[] waitingPoints = new WaitingPoint[taskNum];
        for (int i = 0; i < taskNum; i++) {
            final int j = i;
            waitingPoints[i] = threadPool.add(new Runnable() {
                @Override
                public void run() {
                    flags[j] = true;
                }
            });
        }
        for (WaitingPoint wp: waitingPoints)
            wp.join();

        for (boolean b: flags)
            Assert.assertTrue(b);
    }

    @Test(timeout = 1000)
    public void forced_shutdown(){
        threadPool.add(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100_500);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        threadPool.shutdownNow();
    }

//    @Ignore
    @Test
    // For whatever reason performance of my pool is worse than of separate threads
    public void performance_test() throws InterruptedException {
        final int tasksNumber = 100;
        long poolTime;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    
                }
            }
        };
        poolTime = System.nanoTime();
        for (int i = 0; i < tasksNumber; i++) {
            threadPool.add(runnable);
        }
        threadPool.join();
        poolTime = System.nanoTime() - poolTime;

        long noPoolTime = System.nanoTime();
        Thread[] threads = new Thread[tasksNumber];
        for (int i = 0; i < tasksNumber; i++) {
            threads[i] = new Thread(runnable);
            threads[i].start();
        }
        for (Thread thread: threads)
            thread.join();
        noPoolTime = System.nanoTime() - noPoolTime;

        System.out.println(noPoolTime + " " + poolTime);
        Assert.assertTrue(noPoolTime - poolTime > 0);
    }


}
