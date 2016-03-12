package com.bigfat.md_app_clean.domain.interactor;

import com.bigfat.md_app_clean.data.cache.LoginCache;
import com.bigfat.md_app_clean.data.datastore.LoginDataStore;
import com.bigfat.md_app_clean.data.entity.dbentity.LoginAccountEntity;
import com.bigfat.md_app_clean.data.entity.viewentity.AuthEntity;
import com.bigfat.md_app_clean.data.net.LoginApi;
import com.bigfat.md_app_clean.data.net.NetConstant;
import com.bigfat.md_app_clean.domain.di.PerActivity;
import com.bigfat.md_app_clean.domain.executor.PostExecutionThread;
import com.bigfat.md_app_clean.domain.executor.ThreadExecutor;
import java.util.Date;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by yueban on 16:56 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@PerActivity
public class LoginUseCase extends UseCase {
    private final LoginCache mLoginCache;
    private final LoginDataStore mLoginDataStore;

    @Inject
    public LoginUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
        LoginDataStore loginDataStore, LoginCache loginCache) {
        super(threadExecutor, postExecutionThread, loginDataStore, loginCache);
        mLoginCache = loginCache;
        mLoginDataStore = loginDataStore;
    }

    public Observable<LoginAccountEntity> getLastLoginAccount() {
        return mLoginCache.getLastLoginAccount();
    }

    public void login(final String account, final String password, Subscriber<AuthEntity> subscriber) {
        execute(
            mLoginDataStore.access_token(NetConstant.MD_APP_KEY, NetConstant.MD_APP_SECRET, LoginApi.GrantType.PASSWORD,
                account, password, null, null, null, null).doOnNext(new Action1<AuthEntity>() {
                @Override
                public void call(AuthEntity authEntity) {
                    LoginAccountEntity loginAccountEntity = new LoginAccountEntity();
                    loginAccountEntity.setAccount(account);
                    loginAccountEntity.setPassword(password);
                    loginAccountEntity.setAuthEntity(authEntity);
                    loginAccountEntity.setUpdateTime(new Date());

                    mLoginCache.saveLoginAccount(loginAccountEntity);
                }
            }),
            subscriber);
    }
}
