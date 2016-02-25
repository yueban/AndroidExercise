package com.bigfat.gankio_ca.presentation.common.di.modules;

import android.content.Context;
import com.bigfat.gankio_ca.data.repository.GankDataRepository;
import com.bigfat.gankio_ca.domain.repository.GankRepository;
import com.bigfat.gankio_ca.presentation.common.App;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by yueban on 15:15 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Module
public class ApplicationModule {
    private final App mApp;

    public ApplicationModule(App app) {
        mApp = app;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApp;
    }

    @Provides
    @Singleton
    GankRepository provideGankRepository(GankDataRepository gankDataRepository) {
        return gankDataRepository;
    }
}
