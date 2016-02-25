package com.bigfat.gankio_ca.domain.interactor;

import com.bigfat.gankio_ca.domain.executor.PostExecutionTread;
import com.bigfat.gankio_ca.domain.executor.ThreadExecutor;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

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

    private Subscription mSubscription = Subscriptions.empty();

    /**
     * 工作/UI 线程是通过 Dagger2 注入的,详见实现类构造方法
     */
    public UseCase(ThreadExecutor threadExecutor, PostExecutionTread postExecutionTread) {
        mThreadExecutor = threadExecutor;
        mPostExecutionTread = postExecutionTread;
    }

    /**
     * 返回一个 {@link rx.Observable} ,这个方法需要子类实现,返回子类用例所对应的 {@link rx.Observable}
     */
    protected abstract Observable buildUseCaseObservable();

    @SuppressWarnings("unchecked")
    public void excute(Subscriber useCaseSubscriber) {
        mSubscription = buildUseCaseObservable()
            .subscribeOn(Schedulers.from(mThreadExecutor))
            .observeOn(mPostExecutionTread.getScheduler())
            .subscribe(useCaseSubscriber);
    }

    public void unSubscribe() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
