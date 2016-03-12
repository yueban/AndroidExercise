package com.bigfat.md_app_clean.data.datastore;

import com.bigfat.md_app_clean.data.entity.viewentity.AuthEntity;
import com.bigfat.md_app_clean.data.net.LoginApi;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by yueban on 17:09 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class LoginDataStoreImpl implements LoginDataStore {
    private final LoginApi mLoginApi;

    @Inject
    public LoginDataStoreImpl(Retrofit retrofit) {
        mLoginApi = retrofit.create(LoginApi.class);
    }

    @RxLogObservable
    public Observable<AuthEntity> access_token(String app_key, String app_secret, String grant_type, String account,
        String password, String p_signature, String code, String redirect_uri, String refresh_token) {
        return mLoginApi.access_token(app_key, app_secret, grant_type, account, password, p_signature, code, redirect_uri,
            refresh_token);
    }
}
