package epam.ua.task5.impl;

import epam.ua.task5.WaitingPoint;

/**
 * Created by Adevi on 8/1/2014.
 */
class WaitingPointImpl implements WaitingPoint {
    private final FlaggedRunnable flaggedRunnable;

    public WaitingPointImpl(FlaggedRunnable flaggedRunnable) {
        this.flaggedRunnable = flaggedRunnable;
//        flaggedRunnable.setWaitingPoint(this);
    }

    @Override
    public boolean join() {
        try {
            synchronized (flaggedRunnable) {
                while (!flaggedRunnable.isDone())
                    flaggedRunnable.wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            return flaggedRunnable.hadNoRuntimeExceptions();
        }
    }

}
