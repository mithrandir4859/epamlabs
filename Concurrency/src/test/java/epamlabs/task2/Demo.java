package epamlabs.task2;

import java.util.ArrayList;
import java.util.List;

public class Demo {
	private List<Runnable> tasks = new ArrayList<>();
	private List<String> descriptions = new ArrayList<>();
	private static final int RATIO = 1000000;
	
	public Demo add(Runnable task, String desc){
		tasks.add(task);
		descriptions.add(desc);
		return this;
	}

    public static long measure(Runnable task){
        long time = System.nanoTime();
        task.run();
        return System.nanoTime() - time;
    }

    public static double measure(Runnable task, int howMany){
        long result = 0;
        for (int i = 0; i < howMany; i++)
            result += measure(task);
        return (1. * result)/howMany;
    }

    public void measure() {
        measure(10);
    }
	
	public void measure(int howManyTimes){
        double[] results = new double[tasks.size()];
        for (int i = 0; i < howManyTimes; i++)
            for (int j = 0; j < tasks.size(); j++)
                results[j] += measure(tasks.get(j));
        for (int i = 0; i < tasks.size(); i++)
            System.out.println(descriptions.get(i) + " " + results[i]/howManyTimes);
	}
	
	private long getTime(){
		return System.nanoTime();
	}

}
