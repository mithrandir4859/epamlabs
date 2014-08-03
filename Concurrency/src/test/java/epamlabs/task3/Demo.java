package epamlabs.task3;

import epamlabs.task1.Interval;

import java.util.Random;

/**
 * Created by Adevi on 8/3/2014.
 */
public class Demo {
    public static void main(String[] args) {
        final CircularBuffer<Integer> buffer = new CircularBuffer<>(3);
        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Random r = new Random();
                    for (; ; )
                        try {
                            int num = r.nextInt() % 10;
                            buffer.add(num);
                            System.out.println("added: " + num);
                            Thread.sleep(r.nextInt(7500));
                        } catch (InterruptedException e) {
                            throw new RuntimeException();
                        }
                }
            }).start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Random r = new Random();
                    for (; ; )
                        try {
                            System.err.println("take: " + buffer.take());
                            Thread.sleep(r.nextInt(7500));
                        } catch (InterruptedException e) {
                            throw new RuntimeException();
                        }
                }
            }).start();
        }
    }
}
