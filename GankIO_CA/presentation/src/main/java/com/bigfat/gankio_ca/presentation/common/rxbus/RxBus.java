package com.bigfat.gankio_ca.presentation.common.rxbus;

import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by yueban on 17:47 6/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class RxBus {
    private final Subject<Object, Object> mBus = new SerializedSubject<>(PublishSubject.create());

    @Inject
    public RxBus() {
    }

    public void send(Object o) {
        mBus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }
}
