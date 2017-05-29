package org.marques.anderson.concurrent.testing;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Creates a executor with Fluent Interface :)
 */
public class ConcurrentExecutor {

    private int requests = 1;
    private int threads = 2; // Default number of threads
    private long maxTimeWaiting = Long.MAX_VALUE;

    public ConcurrentExecutor requests(int requests) {
        this.requests = requests;
        return this;
    }

    public static ConcurrentExecutor newInstance() {
        return new ConcurrentExecutor();
    }

    public ConcurrentExecutor threads(int threads) {
        this.threads = threads;
        return this;
    }

    public ConcurrentExecutor timeoutMillis(long maxTimeWaiting) {
        this.maxTimeWaiting = maxTimeWaiting;
        return this;
    }

    public ConcurrentExecutor execute(Callable<Void> callable) {
        ExecutorService threadPool = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < requests; i++) {
            threadPool.submit(callable);
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(maxTimeWaiting, MILLISECONDS);
        } catch (InterruptedException e) {
            new AssertionError(" Timeout exceeded! -> " + maxTimeWaiting);
        }
        return this;
    }
}