package epamlabs.task3;

import java.util.Arrays;

/**
 * Created by Adevi on 8/3/2014.
 */
public class CircularBuffer<E> {
    public static final int DEFAULT_CAPACITY = 100;

    E[] elements;
    private Integer forTake = null;
    private Integer forAdd = 0;

    public CircularBuffer() {
        this(DEFAULT_CAPACITY);
    }

    public CircularBuffer(int capacity) {
        elements = (E[]) new Object[capacity];
    }

    public synchronized void add(E element) throws InterruptedException {
        while (forAdd == null)
            wait();
        assert elements[forAdd] == null;
        elements[forAdd] = element;
        updateAfterInsert();
        notifyAll();
    }

    public synchronized E take() throws InterruptedException {
        while (forTake == null)
            wait();
        E elementForReturn = elements[forTake];
        elements[forTake] = null;
        updateAfterTake();
        notifyAll();
        return elementForReturn;
    }

    private void updateAfterInsert() {
        assert forAdd != null;
        int newForAdd = nextIndex(forAdd);
        if (forTake == null)
            forTake = forAdd;
        forAdd = (newForAdd == forTake) ? null : newForAdd;
    }

    private void updateAfterTake() {
        assert forTake != null;
        int newForTake = nextIndex(forTake);
        if (forAdd == null)
            forAdd = forTake;
        forTake = (newForTake == forAdd) ? null : newForTake;
    }

    private int nextIndex(int index) {
        return (index == elements.length - 1) ? 0 : index + 1;
    }

    public int getCapacity() {
        return elements.length;
    }

    public synchronized boolean isEmpty() {
        return forTake == null;
    }

    public synchronized boolean isFull() {
        return forAdd == null;
    }
}
