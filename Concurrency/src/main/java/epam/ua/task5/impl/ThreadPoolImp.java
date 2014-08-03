package epam.ua.task5.impl;

import epam.ua.task5.ThreadPool;
import epam.ua.task5.WaitingPoint;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Adevi on 8/1/2014.
 */
public class ThreadPoolImp implements ThreadPool {
    private final BlockingQueue<FlaggedRunnable> queue = new LinkedBlockingQueue<>();
    private volatile boolean noMoreTasks = false;

    private final int threadNum;
    private final Thread[] threads;

    public ThreadPoolImp(int threadNum) {
        this.threadNum = threadNum;
        threads = new Thread[threadNum];
        Scheduler scheduler = new Scheduler();
        for (int i = 0; i < threadNum; i++) {
            threads[i] = new Thread(scheduler);
            threads[i].start();
        }
    }

    @Override
    public WaitingPoint add(Runnable r) {
        if (noMoreTasks)
            throw new IllegalStateException("No more tasks are allowed");
        FlaggedRunnable flaggedRunnable = new FlaggedRunnable(r);
        queue.offer(flaggedRunnable);
        return new WaitingPointImpl(flaggedRunnable);
    }

    @Override
    public void shutdown() {
        for (Thread thread : threads)
            thread.interrupt();
        for (Thread thread : threads)
            try {
                thread.join();
            } catch (InterruptedException e) {
                return;
            }
    }

    @Override
    public void shutdownNow() {
        for (Thread thread : threads)
            thread.stop();
        for (Thread thread : threads)
            try {
                thread.join();
            } catch (InterruptedException e) {
                return;
            }
    }

    @Override
    public void join() {
        noMoreTasks = true;
        try {
            for (Thread thread: threads)
                thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private class Scheduler implements Runnable {

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    if (noMoreTasks && queue.isEmpty())
                        return;
                    System.out.println(noMoreTasks + " " + queue.size());
                    Runnable runnable = queue.take();
                    runnable.run();
                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

}
