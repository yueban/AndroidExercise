package com.bigfat.md_app_clean.data.cache;

import com.bigfat.md_app_clean.data.entity.dbentity.LoginAccountEntity;
import com.bigfat.md_app_clean.data.entity.dbentity.LoginAccountEntity_Table;
import com.bigfat.md_app_clean.data.exception.LoginAccountNotExistException;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by yueban on 17:09 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class LoginCacheImpl implements LoginCache {
    private LoginAccountEntity mLoginAccountEntity;
    private Action1<LoginAccountEntity> mLoginAccountEntityAction1;

    @Inject
    public LoginCacheImpl() {
    }

    public Observable<LoginAccountEntity> getLastLoginAccount() {
        if (mLoginAccountEntity == null) {
            mLoginAccountEntity = SQLite
                .select()
                .from(LoginAccountEntity.class)
                .orderBy(LoginAccountEntity_Table.updateTime, false)
                .querySingle();
        }
        if (mLoginAccountEntity != null) {
            return Observable.just(mLoginAccountEntity);
        } else {
            return Observable.error(new LoginAccountNotExistException());
        }
    }

    public Observable<LoginAccountEntity> getLoginAccount(String username) {
        return Observable.just(
            SQLite
                .select()
                .from(LoginAccountEntity.class)
                .where(LoginAccountEntity_Table.account.eq(username))
                .querySingle());
    }

    public void saveLoginAccount(LoginAccountEntity loginAccountEntity) {
        loginAccountEntity.save();
        mLoginAccountEntity = loginAccountEntity;
        if (mLoginAccountEntityAction1 != null) {
            Observable.just(loginAccountEntity).subscribe(mLoginAccountEntityAction1);
        }
    }

    public void setLoginAccountEntityAction1(Action1<LoginAccountEntity> loginAccountEntityAction1) {
        mLoginAccountEntityAction1 = loginAccountEntityAction1;
    }
}
