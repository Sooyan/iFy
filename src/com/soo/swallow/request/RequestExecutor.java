package com.soo.swallow.request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestExecutor {
    
    
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 256;
    private static final int KEEP_ALIVE = 1;
    
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "PriorityExecutor #" + mCount.getAndIncrement());
        }
    };
    
    private final BlockingQueue<Runnable> poolWorkQueue = new PriorityBlockingQueue<Runnable>();
    private final ThreadPoolExecutor threadPoolExecutor;
    
    public RequestExecutor(int coreSize, int maxSize) {
        threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                poolWorkQueue,
                sThreadFactory);
    }

    public <Result> void execute(Request<Result> request) {
        if (request != null) {
            this.threadPoolExecutor.execute(request);
        }
    }

    public int getPoolSize() {
        return threadPoolExecutor.getCorePoolSize();
    }

    public void setPoolSize(int poolSize) {
        if (poolSize > 0) {
            threadPoolExecutor.setCorePoolSize(poolSize);
        }
    }

    public boolean isBusy() {
        return threadPoolExecutor.getActiveCount() >= threadPoolExecutor.getCorePoolSize();
    }

}
