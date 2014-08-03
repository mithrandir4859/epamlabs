package epamlabs.task1;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Adevi on 8/3/2014.
 */
public class TestSummator {
    public static final double DELTA_PERCENTAGE = .01;

    public static double sum(){
        return sum(Summator.DEFAULT_THREAD_NUM);
    }

    public static double sum(int threadNum){
        return Summator.summarize(new Sincos(), new Interval(-10, 10), Summator.DEFAULT_STEP, threadNum);
    }

    @Test
    public void testCorrectness() {
        Assert.assertEquals("Incorrect result", 0d, sum(), .000001);
    }

    public int research(int maxThreads){
        int best = 1;
        long bestTime = Long.MAX_VALUE;
        for (int i = 1; i <= maxThreads; i++){
            long before = System.nanoTime();
            sum(i);
            long after = System.nanoTime();
            if ((after -= before) < bestTime){
                bestTime = after;
                best = i;
            }
        }
        return best;
    }

    @Test
    public void research(){
        double result = 0;
        for (int i = 1; i <= 1000; i++) {
            result += research(50);
            System.out.print(i + "; ");
        }
        System.out.println("\n" + result/1000);
    }
}
