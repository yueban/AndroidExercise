package com.bigfat.gankio_ca.domain.executor;

import rx.Scheduler;

/**
 * UI线程
 * Created by yueban on 11:29 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface PostExecutionTread {
    Scheduler getScheduler();
}
