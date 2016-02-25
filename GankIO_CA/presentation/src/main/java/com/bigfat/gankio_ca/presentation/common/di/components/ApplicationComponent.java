package com.bigfat.gankio_ca.presentation.common.di.components;

import android.content.Context;
import com.bigfat.gankio_ca.domain.repository.GankRepository;
import com.bigfat.gankio_ca.presentation.common.ui.BaseActivity;
import com.bigfat.gankio_ca.presentation.common.di.modules.ApplicationModule;
import com.bigfat.gankio_ca.presentation.common.di.modules.ExecutorModule;
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

    GankRepository gankRepository();
}
