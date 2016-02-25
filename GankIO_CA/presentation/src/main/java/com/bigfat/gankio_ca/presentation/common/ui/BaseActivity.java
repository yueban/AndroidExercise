package com.bigfat.gankio_ca.presentation.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.bigfat.gankio_ca.presentation.common.App;
import com.bigfat.gankio_ca.presentation.common.navigator.Navigator;
import com.bigfat.gankio_ca.presentation.common.di.components.ApplicationComponent;
import com.bigfat.gankio_ca.presentation.common.di.modules.ActivityModule;
import javax.inject.Inject;

/**
 * Created by yueban on 15:43 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class BaseActivity extends AppCompatActivity {
    @Inject
    Navigator mNavigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((App) getApplication()).getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
