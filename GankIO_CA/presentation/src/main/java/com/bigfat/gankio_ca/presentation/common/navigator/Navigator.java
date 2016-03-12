package com.bigfat.gankio_ca.presentation.common.navigator;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.bigfat.gankio_ca.data.entity.GankEntity;
import com.bigfat.gankio_ca.presentation.ui.day.view.DayActivity;
import com.bigfat.gankio_ca.presentation.ui.meizhi.view.MeizhiActivity;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by yueban on 15:44 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class Navigator {
    @Inject
    public Navigator() {
    }

    public void navigateToMain(Context context) {
        if (context != null) {
            Intent intentToLaunch = MeizhiActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToDay(Context context, @NonNull GankEntity gankEntity) {
        if (context != null) {
            Intent intentToLaunch = DayActivity.getCallingIntent(context, gankEntity);
            context.startActivity(intentToLaunch);
        }
    }
}
