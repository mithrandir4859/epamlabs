package epam.ua.task5.impl;


import epam.ua.task5.WaitingPoint;

/**
 * The point of this class is to wrap Runnable instance and notify when run() is done,
 * also provide correct values of flags "done" and "successful".
 */
class FlaggedRunnable implements Runnable{
    private final Runnable runnable;
    private volatile boolean done = false;
    private boolean successful = false;
    private WaitingPoint waitingPoint;

    public void setWaitingPoint(WaitingPoint waitingPoint) {
        if (this.waitingPoint != null)
            throw new IllegalStateException();
        this.waitingPoint = waitingPoint;
    }

    public WaitingPoint getWaitingPoint() {
        return waitingPoint;
    }

    public FlaggedRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public boolean isDone() {
        return done;
    }

    public boolean hadNoRuntimeExceptions() {
        return successful;
    }

    @Override
    public synchronized void run() {
        try {
            runnable.run();
            successful = true;
        } catch (RuntimeException e){
            successful = false;
        } finally {
            done = true;
            notifyAll();
        }
    }

}
