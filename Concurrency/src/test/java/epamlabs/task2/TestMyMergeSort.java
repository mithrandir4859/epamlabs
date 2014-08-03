package epamlabs.task2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.sun.scenario.effect.Merge;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestMyMergeSort {
    public static final int SIZE = 1_000_000;
    private ArrayList<Integer> myMultiThreadSort, mySingleThreadSort,
            javaApiSort;

    @Before
    public void setUp() {
        Random random = new Random(585);
        myMultiThreadSort = new ArrayList<>(SIZE);
        mySingleThreadSort = new ArrayList<>();
        javaApiSort = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            int value;
            myMultiThreadSort.add(value = random.nextInt());
            mySingleThreadSort.add(value);
            javaApiSort.add(value);
        }
    }

    @Test
     public final void testCorrectness() {
        Collections.shuffle(myMultiThreadSort);
        Collections.shuffle(mySingleThreadSort);
        Collections.shuffle(javaApiSort);

        MergeSort.sort(myMultiThreadSort, true);
        MergeSort.sort(mySingleThreadSort, false);
        Collections.sort(javaApiSort);

        Assert.assertEquals(myMultiThreadSort, mySingleThreadSort);
        Assert.assertEquals(myMultiThreadSort, javaApiSort);
    }

    @Test
    public final void testPerformace0() {
        Collections.shuffle(myMultiThreadSort);
        Collections.shuffle(mySingleThreadSort);

        long monoTime = System.nanoTime();
        MergeSort.sort(mySingleThreadSort, false);
        monoTime = System.nanoTime() - monoTime;

        long multiTime = System.nanoTime();
        MergeSort.sort(myMultiThreadSort, true);
        multiTime = System.nanoTime() - multiTime;

        Assert.assertTrue(monoTime > multiTime);

    }

    @Test
    public void newTestPerformance(){
        Demo demo = new Demo();
        final Random random = new Random(5555);

        demo.add(new Runnable() {
            @Override
            public void run() {
                List<Integer> list = new ArrayList<Integer>(SIZE);
                for (int i = 0; i < SIZE; i++)
                    list.add(random.nextInt());
                MergeSort.sort(list, false);
            }
        }, "singlethreaded");

        demo.add(new Runnable() {
            @Override
            public void run() {
                List<Integer> list = new ArrayList<Integer>(SIZE);
                for (int i = 0; i < SIZE; i++)
                    list.add(random.nextInt());
                MergeSort.sort(list, true);
            }
        }, "multithreaded");

        demo.measure();
    }

//    @Test
    public void testPerformance() {
        Demo demo = new Demo();

        demo.add(new Runnable() {

            @Override
            public void run() {
                Collections.sort(javaApiSort);
            }

        }, "java api");

        demo.add(new Runnable() {

            @Override
            public void run() {
                MergeSort.sort(mySingleThreadSort, false);
            }

        }, "single");

        demo.add(new Runnable() {

            @Override
            public void run() {
                MergeSort.sort(myMultiThreadSort, true);
            }

        }, "multi");

        demo.measure();

    }
}
