package epam.ua.task5;

import epam.ua.task5.impl.ThreadPoolImp;

/**
 * Created by Adevi on 8/2/2014.
 */
public class App {
    public static final int THREAD_NUM = 4;
    public static final int SLEEP_TIME = 10;

    public static void main(String[] args) {
        boolean flag = true;
        for (int i = 0; i < 1000; i++) {
            flag &= test();
            System.out.println(i);
        }
        System.out.println(flag);

    }

    public static boolean test(){
        ThreadPool threadPool = new ThreadPoolImp(THREAD_NUM);
        for (int i = 0; i < THREAD_NUM; i++) {
            final int j = i;
            threadPool.add(new Runnable() {
                @Override
                public void run() {
                    while(!Thread.interrupted()) {
//                        System.out.println(j);
                        try {
                            Thread.sleep(SLEEP_TIME);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    Thread.currentThread().interrupt();
                }
            });
        }
        try {
            Thread.sleep(SLEEP_TIME * 10);
        } catch (InterruptedException e) {
            return false;
        }
        threadPool.shutdown();
        return true;
    }
}
