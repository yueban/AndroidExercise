package com.bigfat.gankio_ca.presentation.common.executor;

import com.bigfat.gankio_ca.domain.executor.PostExecutionTread;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by yueban on 15:19 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class UIThread implements PostExecutionTread {
    @Inject
    public UIThread() {
    }

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
