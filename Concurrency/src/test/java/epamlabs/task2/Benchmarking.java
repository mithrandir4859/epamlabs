package epamlabs.task2;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.junit.*;
import org.junit.rules.MethodRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Yurii_Andrieiev on 8/4/2014.
 */
@BenchmarkOptions(benchmarkRounds = 20, warmupRounds = 10)
public class Benchmarking {
    static final Random r = new Random(14);
    static final int SIZE = 1_000_000;
    static final List<Integer> list;

    List<Integer> forSingleThreadSort, forMultiThreadSort, forJavaApiSort;

    @Rule
    public MethodRule benchmarkRun = new BenchmarkRule();

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
    public void singleThreadSort() {
        MergeSort.sort(forSingleThreadSort, false);
    }

    @Test
    public void multiThreadSort() {
        MergeSort.sort(forMultiThreadSort, true);
    }

    @Test
    public void javaApiSort() {
        Collections.sort(forJavaApiSort);
    }

}
