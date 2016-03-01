package com.bigfat.gankio_ca.presentation.common.di.components;

import android.content.Context;
import com.bigfat.gankio_ca.data.cache.GankCache;
import com.bigfat.gankio_ca.data.datasource.GankDataStore;
import com.bigfat.gankio_ca.presentation.common.di.modules.ApplicationModule;
import com.bigfat.gankio_ca.presentation.common.di.modules.ExecutorModule;
import com.bigfat.gankio_ca.presentation.common.ui.BaseActivity;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by yueban on 15:15 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
@Component(modules = { ApplicationModule.class, ExecutorModule.class })
public interface ApplicationComponent extends ExecutorComponent {
    void inject(BaseActivity baseActivity);

    Context context();

    GankDataStore gankDataStore();

    GankCache gankCache();
}
