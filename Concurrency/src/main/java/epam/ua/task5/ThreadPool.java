package epam.ua.task5;

/**
 * Created by Adevi on 8/1/2014.
 */
public interface ThreadPool {
    WaitingPoint add(Runnable r);
    void shutdown();
    void shutdownNow();
    void join();
}
