package com.dj.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorPool {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorPool.class);

    private static final TimeUnit MILLI_SECONDS_TIME_UNIT = TimeUnit.MILLISECONDS;
//    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
//    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
//    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
//    private static final int KEEP_ALIVE = 1;
//
//    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
//        private final AtomicInteger mCount = new AtomicInteger(1);
//
//        public Thread newThread(Runnable r) {
//            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
//        }
//    };
//
//    private static final BlockingQueue<Runnable> sPoolWorkQueue =
//            new LinkedBlockingQueue<Runnable>(128);
//
//    /**
//     * An {@link Executor} that can be used to execute tasks in parallel.
//     */
//    public static final Executor THREAD_POOL_EXECUTOR
//            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
//            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    public static final ExecutorService executorService = Executors.newCachedThreadPool();


    /*public void gracefulShutdown(final ExecutorService service, final Duration timeout)
            throws InterruptedException {
        service.shutdown(); // Disable new tasks from being submitted禁止提交新任务
        final long timeout_in_unit_of_miliseconds = timeout.toMillis();
        // Wait a while for existing tasks to terminate现有任务需要一段时间才能终止
        if (!service.awaitTermination(timeout_in_unit_of_miliseconds, MILLI_SECONDS_TIME_UNIT)) {
            service.shutdownNow(); // Cancel currently executing tasks  
            // 取消当前正在执行的任务

            // Wait a while for tasks to respond to being cancelled等待任务响应被取消
            if (!service.awaitTermination(timeout_in_unit_of_miliseconds, MILLI_SECONDS_TIME_UNIT)) {
                logger.error("The executor service did not terminate.");
            }
        }
    }*/

}
