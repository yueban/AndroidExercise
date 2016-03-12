package com.bigfat.md_app_clean.domain.executor;

import java.util.concurrent.ExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 工作线程
 *
 * Created by yueban on 15:10 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class WorkExecutor implements ThreadExecutor {
    private final ExecutorService mExecutorService;

    @Inject
    public WorkExecutor(ExecutorService executorService) {
        mExecutorService = executorService;
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new IllegalArgumentException("Runnable to execute cannot be null");
        }
        mExecutorService.execute(command);
    }
}
