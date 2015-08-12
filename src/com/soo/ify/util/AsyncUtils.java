package com.soo.ify.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class AsyncUtils {
    
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;

    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncUtils #" + mCount.getAndIncrement());
        }
    };

    private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
            sPoolWorkQueue, sThreadFactory);
    
    
    private static InnerCallback callback = new InnerCallback();
    private static final Handler handler = new Handler(Looper.getMainLooper(), callback);
    

    public static void executeRunnable(Runnable runnable) {
        THREAD_POOL_EXECUTOR.execute(runnable);
    }
    
    public static void postRunnable(Runnable runnable) {
        handler.post(runnable);
    }
    
    public static void postDelayRunnable(Runnable runnable, long delaymillis) {
        handler.postDelayed(runnable, delaymillis);
    }

    public static void postObject(Object obj, Handler.Callback callback) {
        postObject(obj, callback, 0);
    }
    
    public static void postObject(Object obj, Handler.Callback callback, long delayTime) {
        if (obj != null && callback != null) {
            InnerCallback.putOC(obj, callback);
            if (obj != null && callback != null) {
                InnerCallback.putOC(obj, callback);
                executeRunnable(new InnerRunnable(handler, obj, delayTime));
            }
        }
    }
    
    static class InnerRunnable implements Runnable {
        
        final Handler handler;
        final Object obj;
        final long delayMillis;
        
        public InnerRunnable(Handler handler, Object obj, long delayMillis) {
            this.handler = handler;
            this.obj = obj;
            this.delayMillis = delayMillis;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.obtainMessage(1, obj).sendToTarget();
        }
    }
    
    static class InnerCallback implements Handler.Callback {
        
        static Map<Object, Handler.Callback> maps = new HashMap<Object, Handler.Callback>();
        
        public static void putOC(Object obj, Handler.Callback callback) {
            maps.put(obj, callback);
        }
        
        InnerCallback() {
        }

        @Override
        public boolean handleMessage(Message msg) {
            Handler.Callback callback = maps.get(msg.obj);
            if (callback != null) {
                callback.handleMessage(msg);
                maps.remove(msg.obj);
            }
            return false;
        }
    }
}
