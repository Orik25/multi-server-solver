package com.orik.applicationserver.component;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ThreadPool {
    private ExecutorService executorService;
    private LinkedBlockingQueue<Runnable> taskQueue;
    private Map<Long, Integer> taskIndexMap = new ConcurrentHashMap<>();
    private Set<Long> tasksInQueue = ConcurrentHashMap.newKeySet();

    private Map<Long,Long> timeStart = new HashMap<>();


    private final int NUMBER_OF_THREADS = 5;

    public ThreadPool() {

        taskQueue = new LinkedBlockingQueue<>(10);
        executorService = new ThreadPoolExecutor(
                NUMBER_OF_THREADS, NUMBER_OF_THREADS, 0L, TimeUnit.MILLISECONDS, taskQueue);
    }

    public Future<Long> executeTask(int index,Long taskId) {
        FibonacciTask task = new FibonacciTask(index,taskId);
        tasksInQueue.add(taskId);
        taskIndexMap.put(taskId,index);

        return executorService.submit(task);
    }

    public int getActiveThreadCount() {
        return ((ThreadPoolExecutor) executorService).getActiveCount();
    }

    public int getQueueSize() {
        return taskQueue.size();
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public class FibonacciTask implements Callable<Long> {
        private int index;
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        private final Long taskId;

        public FibonacciTask(int index, Long taskId) {
            this.index = index;
            this.taskId = taskId;
        }
        public int getIndex() {
            return index;
        }
        public void cancelTask() {
            cancelled.set(true);
        }
        @Override
        public Long call(){
            tasksInQueue.remove(taskId);
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }
//            System.out.println("call"+index);
            timeStart.put(taskId, (long) (System.nanoTime()/1e9));
            Long startTime = (long) (System.nanoTime()/1e9);
            Long temp = calculateFibonacci(index);
            Long endTime = (long) (System.nanoTime()/1e9);
            return temp;
        }


        private long calculateFibonacci(int n) {
            long temp = 0;
            if (n <= 1) {
                return n;
            } else {
                for (int i = 2; i <= n; i++) {
                    if (cancelled.get()) {
                        return -1;
                    }
                    else if (Thread.currentThread().isInterrupted()) {
                        return -1;
                    }
                    temp = calculateFibonacci(i - 1) + calculateFibonacci(i - 2);
                }
                Long endTime = (long) (System.nanoTime()/1e9);

                return temp;
            }
        }


    }

    public Map<Long, Long> getTimeStart() {
        return timeStart;
    }

    public int getIndexFromTask(Long taskId) {
        return taskIndexMap.get(taskId);
    }

    public boolean isInQueue(Long id){
        return tasksInQueue.contains(id);
    }
}