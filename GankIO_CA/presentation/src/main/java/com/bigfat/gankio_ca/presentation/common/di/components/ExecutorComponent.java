package com.bigfat.gankio_ca.presentation.common.di.components;

import com.bigfat.gankio_ca.domain.executor.PostExecutionTread;
import com.bigfat.gankio_ca.domain.executor.ThreadExecutor;
import com.bigfat.gankio_ca.presentation.common.di.modules.ExecutorModule;
import dagger.Component;
import java.util.concurrent.ExecutorService;
import javax.inject.Singleton;

/**
 * Created by yueban on 23:06 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
@Component(modules = ExecutorModule.class)
public interface ExecutorComponent {
    ThreadExecutor threadExecutor();

    ExecutorService executorService();

    PostExecutionTread postExecutionThread();
}
