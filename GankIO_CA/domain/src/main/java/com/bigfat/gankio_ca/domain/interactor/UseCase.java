package com.bigfat.gankio_ca.domain.interactor;

import com.bigfat.gankio_ca.domain.executor.PostExecutionTread;
import com.bigfat.gankio_ca.domain.executor.ThreadExecutor;
import java.util.HashSet;
import java.util.Set;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * 用例类,也是CleanArchitecture中的连接器,连接data层与presentation层.
 * 在本项目中通过UseCase结合RxJava分离 工作/UI 线程
 * 每一个具体的用例都应继承这个抽象类
 *
 * Created by yueban on 13:05 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public abstract class UseCase {
    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionTread mPostExecutionTread;

    private Set<Subscription> mSubscriptions = new HashSet<>();

    /**
     * 工作/UI 线程是通过 Dagger2 注入的,详见实现类构造方法
     */
    public UseCase(ThreadExecutor threadExecutor, PostExecutionTread postExecutionTread) {
        mThreadExecutor = threadExecutor;
        mPostExecutionTread = postExecutionTread;
    }

    protected <T> void execute(Observable<T> observable, Subscriber<T> useCaseSubscriber) {
        Subscription subscription = observable
            .subscribeOn(Schedulers.from(mThreadExecutor))
            .observeOn(mPostExecutionTread.getScheduler())
            .subscribe(useCaseSubscriber);
        mSubscriptions.add(subscription);
    }

    public void unSubscribe() {
        for (Subscription subscription : mSubscriptions) {
            if (subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }
}
