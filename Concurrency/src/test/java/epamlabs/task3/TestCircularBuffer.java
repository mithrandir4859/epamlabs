package epamlabs.task3;

import epamlabs.task1.Interval;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Adevi on 8/3/2014.
 */
public class TestCircularBuffer {
    private static final int CAPACITY = 1000;
    private final CircularBuffer<Integer> buffer = new CircularBuffer<>(CAPACITY);
    private Random random = new Random(100500);

    @Test
    public void keeps_elemements() throws InterruptedException {
        for (int i = 0; i < CAPACITY; i++)
            buffer.add(i);
        for (int i = 0; i < CAPACITY; i++)
            Assert.assertEquals(Integer.valueOf(i), buffer.take());
    }

    @Test
    public void many_threads() throws InterruptedException {
        final int extraCapacity = CAPACITY * 10;
        final Integer[] elements = new Integer[extraCapacity];
        for (int i = 0; i < extraCapacity; i++)
            elements[i] = random.nextInt();
        final AtomicInteger atomicCounter = new AtomicInteger(0);
        Runnable producer = new Runnable() {
            @Override
            public void run() {
                while (atomicCounter.get() < extraCapacity) {
                    try {
                        buffer.add(elements[atomicCounter.getAndIncrement()]);
                    } catch (InterruptedException e) {
                        throw new RuntimeException();
                    }
                }
            }
        };
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(producer);
            thread.start();
        }
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < extraCapacity; i++) {
            int value = buffer.take().intValue();
            set.add(value);
        }
        Assert.assertEquals(set, new HashSet<Integer>(Arrays.<Integer>asList(elements)));
    }


}
