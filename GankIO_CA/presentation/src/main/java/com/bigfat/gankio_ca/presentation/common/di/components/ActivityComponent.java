package com.bigfat.gankio_ca.presentation.common.di.components;

import android.app.Activity;
import com.bigfat.gankio_ca.presentation.common.di.PerActivity;
import com.bigfat.gankio_ca.presentation.common.di.modules.ActivityModule;
import dagger.Component;

/**
 * Created by yueban on 16:07 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity activity();
}
