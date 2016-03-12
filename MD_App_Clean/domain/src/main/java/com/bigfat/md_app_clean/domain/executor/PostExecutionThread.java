package com.bigfat.md_app_clean.domain.executor;

import rx.Scheduler;

/**
 * Created by yueban on 15:10 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface PostExecutionThread {
    Scheduler getScheduler();
}
