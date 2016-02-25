package com.bigfat.gankio_ca.presentation.common.di.modules;

import android.app.Activity;
import com.bigfat.gankio_ca.presentation.common.di.PerActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by yueban on 16:07 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @PerActivity
    Activity provideActivity() {
        return mActivity;
    }
}
