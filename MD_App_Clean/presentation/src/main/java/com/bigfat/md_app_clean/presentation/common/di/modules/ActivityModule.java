package com.bigfat.md_app_clean.presentation.common.di.modules;

import android.app.Activity;
import com.bigfat.md_app_clean.domain.di.PerActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by yueban on 15:45 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @PerActivity
    @Provides
    Activity provideActivity() {
        return mActivity;
    }
}
