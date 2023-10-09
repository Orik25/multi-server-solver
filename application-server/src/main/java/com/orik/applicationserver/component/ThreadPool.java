package com.orik.applicationserver.component;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ThreadPool {
    private ExecutorService executorService;
    private LinkedBlockingQueue<Runnable> taskQueue;

    private Set<Long> executingTasks = ConcurrentHashMap.newKeySet();


    private final int NUMBER_OF_THREADS = 1;

    public ThreadPool() {

        taskQueue = new LinkedBlockingQueue<>(10);
        executorService = new ThreadPoolExecutor(
                NUMBER_OF_THREADS, NUMBER_OF_THREADS, 0L, TimeUnit.MILLISECONDS, taskQueue);
    }

    public Future<Long> executeTask(int index,Long taskId) {
        FibonacciTask task = new FibonacciTask(index,taskId);
        executingTasks.add(taskId);

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
        private final int index;
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        private final Long taskId;

        public FibonacciTask(int index, Long taskId) {
            this.index = index;
            this.taskId = taskId;
        }
        public void cancelTask() {
            cancelled.set(true);
        }
        @Override
        public Long call(){
            executingTasks.remove(taskId);
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }
            System.out.println("call"+index);

            return calculateFibonacci(index);
        }


        private long calculateFibonacci(int n) {
            long temp = 0; // Оголосити temp перед циклом

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
                return temp;
            }
        }


    }
    public boolean isInQueue(Long id){
        return executingTasks.contains(id);
    }
}