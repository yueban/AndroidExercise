package com.bigfat.md_app_clean.domain.di.components;

import com.bigfat.md_app_clean.domain.di.modules.ExecutorModule;
import com.bigfat.md_app_clean.domain.executor.PostExecutionThread;
import com.bigfat.md_app_clean.domain.executor.ThreadExecutor;
import dagger.Component;
import java.util.concurrent.ExecutorService;
import javax.inject.Singleton;

/**
 * Created by yueban on 15:09 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
@Component(modules = ExecutorModule.class)
public interface ExecutorComponent {
    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    ExecutorService executorService();
}
