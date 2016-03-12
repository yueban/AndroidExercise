package com.bigfat.md_app_clean.presentation.common.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.bigfat.md_app_clean.presentation.common.App;
import com.bigfat.md_app_clean.presentation.common.di.HasActivityComponent;
import com.bigfat.md_app_clean.presentation.common.di.components.ActivityComponent;
import com.bigfat.md_app_clean.presentation.common.di.components.ApplicationComponent;
import com.bigfat.md_app_clean.presentation.common.di.components.DaggerActivityComponent;
import com.bigfat.md_app_clean.presentation.common.di.components.UtilComponent;
import com.bigfat.md_app_clean.presentation.common.di.modules.ActivityModule;
import com.bigfat.md_app_clean.presentation.common.navigator.Navigator;
import javax.inject.Inject;

/**
 * Created by yueban on 15:24 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class BaseActivity extends AppCompatActivity implements HasActivityComponent {
    protected ActivityComponent mActivityComponent;

    @Inject Navigator mNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getApplicationComponent().inject(this);

        mActivityComponent = DaggerActivityComponent.builder()
            .applicationComponent(getApplicationComponent())
            .activityModule(getActivityModule())
            .build();
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((App) getApplication()).getApplicationComponent();
    }

    public UtilComponent getUtilComponent() {
        return ((App) getApplication()).getUtilComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }
}
