package com.yueban.installedpackage.rx;

import rx.Subscriber;

/**
 * @author yueban
 * @date 2017/1/17
 * @email fbzhh007@gmail.com
 */
public abstract class SimpleSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }
}
