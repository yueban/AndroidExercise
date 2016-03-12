package com.bigfat.md_app_clean.presentation.common.di.components;

import android.app.Activity;
import com.bigfat.md_app_clean.domain.di.PerActivity;
import com.bigfat.md_app_clean.presentation.common.di.modules.ActivityModule;
import com.bigfat.md_app_clean.presentation.ui.login.LoginActivity;
import dagger.Component;

/**
 * Created by yueban on 15:45 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(LoginActivity loginActivity);

    Activity activity();
}
