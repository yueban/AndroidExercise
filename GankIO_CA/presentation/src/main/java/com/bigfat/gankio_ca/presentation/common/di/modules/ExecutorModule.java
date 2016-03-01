package com.bigfat.gankio_ca.presentation.common.di.modules;

import com.bigfat.gankio_ca.domain.executor.WorkExecutor;
import com.bigfat.gankio_ca.domain.executor.PostExecutionTread;
import com.bigfat.gankio_ca.domain.executor.ThreadExecutor;
import com.bigfat.gankio_ca.presentation.common.executor.UIThread;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Singleton;

/**
 * Created by yueban on 23:07 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Module
public class ExecutorModule {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = 10;
    private static final int KEEP_ALIVE_TIME = 10;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(WorkExecutor workExecutor) {
        return workExecutor;
    }

    @Provides
    @Singleton
    ExecutorService provideExecutorService() {
        BlockingQueue<Runnable> poolWorkQueue = new LinkedBlockingQueue<>(128);
        ThreadFactory threadFactory = new WorkThreadFactory();
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, poolWorkQueue,
            threadFactory);
    }

    @Provides
    @Singleton
    PostExecutionTread providePostExecutionTread(UIThread uiThread) {
        return uiThread;
    }

    private static final class WorkThreadFactory implements ThreadFactory {
        private static final String THREAD_NAME = "MING_DAO #";
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, THREAD_NAME + mCount.getAndIncrement());
        }
    }
}
