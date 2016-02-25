package com.bigfat.gankio_ca.presentation.main.di;

import com.bigfat.gankio_ca.domain.executor.PostExecutionTread;
import com.bigfat.gankio_ca.domain.executor.ThreadExecutor;
import com.bigfat.gankio_ca.domain.interactor.GetDataUseCase;
import com.bigfat.gankio_ca.domain.interactor.UseCase;
import com.bigfat.gankio_ca.domain.repository.GankRepository;
import com.bigfat.gankio_ca.presentation.common.di.PerActivity;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

/**
 * Created by yueban on 16:16 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Module
public class GankMainModule {
    private final String type;
    private final int pageIndex;

    public GankMainModule(String type, int pageIndex) {
        this.type = type;
        this.pageIndex = pageIndex;
    }

    @Provides
    @PerActivity
    @Named("GetDataUseCase")
    UseCase provideGetDataUseCase(ThreadExecutor threadExecutor, PostExecutionTread postExecutionTread,
        GankRepository gankRepository) {
        return new GetDataUseCase(threadExecutor, postExecutionTread, gankRepository, type, pageIndex);
    }
}
