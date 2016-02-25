package com.bigfat.gankio_ca.data.executor;

import com.bigfat.gankio_ca.domain.executor.ThreadExecutor;
import java.util.concurrent.ExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 工作线程
 *
 * Created by yueban on 13:26 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class WorkExecutor implements ThreadExecutor {
    private final ExecutorService mExecutorService;

    @Inject
    public WorkExecutor(ExecutorService executorService) {
        this.mExecutorService = executorService;
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new IllegalArgumentException("Runnable to execute cannot be null");
        }
        mExecutorService.execute(command);
    }
}
