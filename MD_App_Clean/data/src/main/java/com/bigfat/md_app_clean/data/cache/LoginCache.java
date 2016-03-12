package com.bigfat.md_app_clean.data.cache;

import com.bigfat.md_app_clean.data.entity.dbentity.LoginAccountEntity;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by yueban on 17:09 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface LoginCache {
    Observable<LoginAccountEntity> getLastLoginAccount();

    Observable<LoginAccountEntity> getLoginAccount(String username);

    void saveLoginAccount(LoginAccountEntity loginAccountEntity);

    void setLoginAccountEntityAction1(Action1<LoginAccountEntity> loginAccountEntitySubscriber);
}
