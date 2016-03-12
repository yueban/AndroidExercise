package com.bigfat.md_app_clean.domain.interactor;

import android.util.Log;
import com.bigfat.md_app_clean.data.cache.LoginCache;
import com.bigfat.md_app_clean.data.datastore.LoginDataStore;
import com.bigfat.md_app_clean.data.entity.dbentity.LoginAccountEntity;
import com.bigfat.md_app_clean.data.entity.viewentity.AuthEntity;
import com.bigfat.md_app_clean.data.exception.APIException;
import com.bigfat.md_app_clean.data.exception.ApiErrorModel;
import com.bigfat.md_app_clean.data.net.LoginApi;
import com.bigfat.md_app_clean.data.net.NetConstant;
import com.bigfat.md_app_clean.domain.executor.PostExecutionThread;
import com.bigfat.md_app_clean.domain.executor.ThreadExecutor;
import java.util.HashSet;
import java.util.Set;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yueban on 16:47 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public abstract class UseCase {
    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;
    private final LoginDataStore mLoginDataStore;
    private final LoginCache mLoginCache;
    private final Set<Subscription> mSubscriptions = new HashSet<>();

    protected UseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, LoginDataStore store,
        LoginCache cache) {
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
        mLoginDataStore = store;
        mLoginCache = cache;
    }

    protected <T> void execute(final Observable<T> observable, final Subscriber<T> subscriber) {
        Subscription subscription = observable
            .subscribeOn(Schedulers.from(mThreadExecutor))
            .observeOn(mPostExecutionThread.getScheduler())
            .onErrorResumeNext(new Func1<Throwable, Observable<? extends T>>() {
                @Override
                public Observable<? extends T> call(Throwable throwable) {
                    if (throwable instanceof APIException) {
                        ApiErrorModel apiErrorModel = ((APIException) throwable).getApiErrorModel();
                        switch (apiErrorModel.getError_code()) {
                            case NetConstant.ERROR_CODE_TOKEN_INVALID:
                            case NetConstant.ERROR_CODE_PARAMETER_LACK:
                                return mLoginCache.getLastLoginAccount()
                                    .flatMap(new Func1<LoginAccountEntity, Observable<AuthEntity>>() {
                                        @Override
                                        public Observable<AuthEntity> call(LoginAccountEntity loginAccountEntity) {
                                            Log.d("Observable<AuthEntity>", "Observable<AuthEntity>");
                                            return mLoginDataStore.access_token(NetConstant.MD_APP_KEY, NetConstant.MD_APP_SECRET,
                                                LoginApi.GrantType.REFRESH_TOKEN, null, null, null, null, null,
                                                loginAccountEntity.getAuthEntity().getRefresh_token())
                                                .subscribeOn(Schedulers.from(mThreadExecutor))
                                                .observeOn(mPostExecutionThread.getScheduler());
                                        }
                                    })
                                    .flatMap(new Func1<AuthEntity, Observable<T>>() {
                                        @Override
                                        public Observable<T> call(AuthEntity authEntity) {
                                            return observable;
                                        }
                                    })
                                    .subscribeOn(Schedulers.from(mThreadExecutor))
                                    .observeOn(mPostExecutionThread.getScheduler());

                            case NetConstant.ERROR_CODE_TOKEN_NOT_EXIST:

                                break;
                        }
                    }
                    return null;
                }
            })
            .subscribe(subscriber);

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
