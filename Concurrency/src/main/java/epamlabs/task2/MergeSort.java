package epamlabs.task2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort {
    public static final int RATIO = 1;
    public static final int THRESHOLD = 100000;

    public static <E extends Comparable<E>> void sort(List<E> list) {
        sort(list, true);
    }

    public static <E extends Comparable<E>> void sort(List<E> list, boolean multithreaded) {
        sort(list, new Comparator<E>() {

            @Override
            public int compare(E o1, E o2) {
                if (o1 == o2)
                    return 0;
                if (o1 == null)
                    return 1;
                if (o2 == null)
                    return -1;
                return o1.compareTo(o2);
            }
        }, multithreaded);
    }

    public static <E> void sort(List<E> list, Comparator<E> c) {
        sort(list, c, true);
    }

    public static <E> void sort(List<E> list, Comparator<E> c,
                                boolean multithreaded) {
        int processors = Runtime.getRuntime().availableProcessors();
        int numOfthreads = multithreaded && processors != 1 ? processors
                * RATIO : 1;
        doSort(list, c, numOfthreads);
    }

    private static <E> void doSort(List<E> list, final Comparator<E> c,
                                   int numOfThreads) {

        if (list.size() < 2)
            return;
        if (list.size() == 2) {
            if (c.compare(list.get(0), list.get(1)) > 0)
                swap(list, 0, 1);
            return;
        }

        int middleIndex = list.size() / 2;
        final List<E> leftList = new ArrayList<>(list.subList(0, middleIndex));
        final List<E> rightList = new ArrayList<>(list.subList(middleIndex,
                list.size()));

        if (numOfThreads == 1 || rightList.size() < THRESHOLD) {
            doSort(leftList, c, numOfThreads);
            doSort(rightList, c, numOfThreads);
        } else {
            final int newNumOfThreads = numOfThreads / 2;
            Thread leftThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    doSort(leftList, c, newNumOfThreads);
                }
            });
            /*Thread rightThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    doSort(rightList, c, newNumOfThreads);
                }
            });*/
            leftThread.start();

//            rightThread.start();
            doSort(rightList, c, newNumOfThreads);
            joinLoop: for (; ; ) {
                try {
                    leftThread.join();
//                    rightThread.join();
                    break joinLoop;
                } catch (InterruptedException neverHere) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        merge(list, c, leftList, rightList);

    }

    private static <E> void swap(List<E> list, int i, int j) {
        E temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    private static <E> void merge(List<E> list, Comparator<E> c, List<E> leftList, List<E> rightList) {
        int leftIndex = 0, rightIndex = 0, mainIndex = 0;
        while (leftIndex < leftList.size() && rightIndex < rightList.size()) {
            E leftElement = leftList.get(leftIndex);
            E rightElement = rightList.get(rightIndex);
            if (c.compare(leftElement, rightElement) < 0) {
                list.set(mainIndex, leftElement);
                leftIndex++;
            } else {
                list.set(mainIndex, rightElement);
                rightIndex++;
            }
            mainIndex++;
        }

        for (; leftIndex < leftList.size(); leftIndex++)
            list.set(mainIndex++, leftList.get(leftIndex));

        for (; rightIndex < rightList.size(); rightIndex++)
            list.set(mainIndex++, rightList.get(rightIndex));

    }

}
