package com.bigfat.gankio_ca.presentation.main.di;

import com.bigfat.gankio_ca.presentation.common.di.PerActivity;
import com.bigfat.gankio_ca.presentation.common.di.components.ActivityComponent;
import com.bigfat.gankio_ca.presentation.common.di.components.ApplicationComponent;
import com.bigfat.gankio_ca.presentation.common.di.modules.ActivityModule;
import com.bigfat.gankio_ca.presentation.main.view.MainActivityFragment;
import dagger.Component;

/**
 * Created by yueban on 16:14 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = { ActivityModule.class, GankMainModule.class })
public interface GankMainComponent extends ActivityComponent {
    void inject(MainActivityFragment mainActivityFragment);
}
