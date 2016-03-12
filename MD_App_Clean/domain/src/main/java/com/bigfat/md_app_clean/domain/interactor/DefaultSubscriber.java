package com.bigfat.md_app_clean.domain.interactor;

/**
 * {@link rx.Subscriber} 基类,需要 presentation 层实现具体的操作类
 *
 * Created by yueban on 13:16 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class DefaultSubscriber<T> extends rx.Subscriber<T> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {
    }
}
