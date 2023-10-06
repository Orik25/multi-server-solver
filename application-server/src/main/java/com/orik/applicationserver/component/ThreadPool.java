package com.orik.applicationserver.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;


import java.util.concurrent.*;

@Component
public
class ThreadPool {
    private ExecutorService executorService;
    private BlockingQueue<Runnable> taskQueue;

    private final int NUMBER_OF_THREADS = 5;

    public ThreadPool() {
        taskQueue = new LinkedBlockingQueue<>();
        executorService = new ThreadPoolExecutor(
                NUMBER_OF_THREADS, NUMBER_OF_THREADS, 0L, TimeUnit.MILLISECONDS, taskQueue);
    }

    public Long executeTask(int index) throws ExecutionException, InterruptedException {
        return executorService.submit(new FibonacciTask(index)).get();
    }

    public int getActiveThreadCount() {
        return ((ThreadPoolExecutor) executorService).getActiveCount();
    }

    public void shutdown() {
        executorService.shutdown();
    }
    private class FibonacciTask implements Callable<Long> {
        private int index;

        public FibonacciTask(int index) {
            this.index = index;
        }

        @Override
        public Long call() throws Exception {
            System.out.println("call");
            return calculateFibonacci(index);
        }

        private long calculateFibonacci(int n) {
            if (n <= 1) {
                return n;
            } else {
                return calculateFibonacci(n - 1) + calculateFibonacci(n - 2);
            }
        }
    }

}


