package epamlabs.task5;

import java.util.concurrent.Executor;

/**
 * Created by Yurii_Andrieiev on 8/4/2014.
 */
public class ThreadPool implements Executor {
    public static final int DEFAULT_THREAD_NUM = Runtime.getRuntime().availableProcessors() * 4;
    final private int threadNum;

    public ThreadPool() {
        this(DEFAULT_THREAD_NUM);
    }

    public ThreadPool(int threadNum) {
        this.threadNum = threadNum;
    }

    @Override
    public void execute(Runnable command) {


    }
}
