package epamlabs.task2;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Yurii_Andrieiev on 8/4/2014.
 */
public class CorrectnessTest {
    static final Random r = new Random(14);
    static final int SIZE = 100_000;
    static final List<Integer> list;

    List<Integer> forSingleThreadSort, forMultiThreadSort, forJavaApiSort;

    static {
        list = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++)
            list.add(r.nextInt());
    }

    @Before
    public void prepare() {
        forMultiThreadSort = new ArrayList<>(list);
        forSingleThreadSort = new ArrayList<>(list);
        forJavaApiSort = new ArrayList<>(list);
    }

    @Test
    public void testCorrectness() {
        Collections.shuffle(forSingleThreadSort);
        Collections.shuffle(forMultiThreadSort);
        Collections.shuffle(forJavaApiSort);

        MergeSort.sort(forMultiThreadSort, true);
        MergeSort.sort(forSingleThreadSort, false);
        Collections.sort(forJavaApiSort);

        Assert.assertEquals(forMultiThreadSort, forSingleThreadSort);
        Assert.assertEquals(forMultiThreadSort, forJavaApiSort);
    }
}
