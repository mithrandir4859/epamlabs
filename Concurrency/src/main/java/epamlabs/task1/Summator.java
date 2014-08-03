package epamlabs.task1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adevi on 8/3/2014.
 */
public class Summator {
//    public static final double DEFAULT_STEP = .0001;
    public static final double DEFAULT_STEP = 1.0/4096;

    public static final int DEFAULT_THREAD_NUM = Runtime.getRuntime().availableProcessors() * 4;

    public static double summarize(MathFunction f, Interval interval){
        return summarize(f, interval, DEFAULT_STEP);
    }

    public static double summarize(MathFunction f, Interval interval, double step){
        return summarize(f, interval, step, DEFAULT_THREAD_NUM);
    }

    public static double summarize(MathFunction f, Interval interval, int threadNum){
        return summarize(f, interval, DEFAULT_STEP, threadNum);
    }

    public static double summarize(final MathFunction f, Interval interval, final double step, int threadNum){
        Thread[] threads = new Thread[threadNum];
        final List<Interval> intervals = split(interval, threadNum);
        final double[] results = new double[threadNum];
        class SummingTask implements Runnable{
            final int i;

            public SummingTask(int i) {
                this.i = i;
            }

            @Override
            public void run() {
                Interval currentInterval = intervals.get(i);
                for (double arg = currentInterval.getStart(); arg < currentInterval.getEnd(); arg += step)
                    results[i] += f.apply(arg);
            }
        }
        for (int i = 0; i < threadNum; i++) {
            threads[i] = new Thread(new SummingTask(i));
            threads[i].start();
        }

        for (int i = 0; i < threadNum; ) {
            try {
                threads[i].join();
                i++;
            } catch (InterruptedException e) {
                // for simplicity my method is NOT cancellable
                // but I propagate interrupted status up the stack
                Thread.currentThread().interrupt();
            }
        }

        double result = 0;
        for (Double partialResult: results)
            result += partialResult;
        return result += f.apply(interval.getEnd());
    }

    private static List<Interval> split(Interval interval, int threadNum){
        List<Interval> intervals = new ArrayList<>(threadNum);
        double step = interval.getLength() / threadNum;
        double currentStart = interval.getStart();
        for (int i = 0; i < threadNum; i++)
            intervals.add(new Interval(currentStart, currentStart += step));
        return intervals;
    }

    private static double summarizeMonoThread(MathFunction f, Interval interval, double step){
        double result = 0;
        for (double arg = interval.getStart(); arg <= interval.getEnd(); arg += step)
            result += f.apply(arg);
        return result;
    }

}
