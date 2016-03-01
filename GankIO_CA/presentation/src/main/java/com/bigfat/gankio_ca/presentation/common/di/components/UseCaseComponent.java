package com.bigfat.gankio_ca.presentation.common.di.components;

import com.bigfat.gankio_ca.presentation.common.di.PerActivity;
import com.bigfat.gankio_ca.presentation.common.di.modules.ActivityModule;
import com.bigfat.gankio_ca.presentation.common.di.modules.UseCaseModule;
import com.bigfat.gankio_ca.presentation.ui.meizhi.view.MeizhiFragment;
import dagger.Component;

/**
 * Created by yueban on 17:32 28/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = { ActivityModule.class, UseCaseModule.class })
public interface UseCaseComponent extends ActivityComponent{
    void inject(MeizhiFragment mainActivityFragment);
}
