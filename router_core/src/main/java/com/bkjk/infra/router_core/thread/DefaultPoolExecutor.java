package com.bkjk.infra.router_core.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: zhouzhenhua
 * Date: 2018/9/29
 * Version: 1.0.0
 * Description:
 */
public class DefaultPoolExecutor {

    // CPU 核数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // 最大线程数为 CPU 核数 + 1
    private static final int MAX_CORE_POOL_SIZE = CPU_COUNT + 1;
    // 存活 30 秒 回收线程池中的线程
    private static final long KEEP_ALIVE_THREAD_LIFE = 30L;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {

        private final AtomicInteger mAtomicInteger = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "HenryRouter #" + mAtomicInteger.getAndIncrement());
        }
    };

    public static ThreadPoolExecutor getDefaultPoolExecutor(int corePoolSize) {
        if (0 == corePoolSize) {
            return null;
        }
        corePoolSize = Math.min(corePoolSize, MAX_CORE_POOL_SIZE);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize,
                KEEP_ALIVE_THREAD_LIFE, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(64), sThreadFactory);
        // 核心线程也会被销毁
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }
}