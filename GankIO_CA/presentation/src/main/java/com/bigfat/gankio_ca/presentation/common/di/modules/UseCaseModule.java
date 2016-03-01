package com.bigfat.gankio_ca.presentation.common.di.modules;

import com.bigfat.gankio_ca.data.cache.GankCache;
import com.bigfat.gankio_ca.data.datasource.GankDataStore;
import com.bigfat.gankio_ca.domain.executor.PostExecutionTread;
import com.bigfat.gankio_ca.domain.executor.ThreadExecutor;
import com.bigfat.gankio_ca.domain.interactor.GankUseCase;
import com.bigfat.gankio_ca.presentation.common.di.PerActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by yueban on 17:32 28/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Module
public class UseCaseModule {
    @Provides
    @PerActivity
    GankUseCase provideGankUseCase(ThreadExecutor threadExecutor, PostExecutionTread postExecutionTread,
        GankDataStore gankDataStore, GankCache gankCache) {
        return new GankUseCase(threadExecutor, postExecutionTread, gankDataStore, gankCache);
    }
}
