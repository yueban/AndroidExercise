package com.bigfat.md_app_clean.data.datastore;

import com.bigfat.md_app_clean.data.entity.viewentity.AuthEntity;
import rx.Observable;

/**
 * Created by yueban on 17:09 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface LoginDataStore {
    Observable<AuthEntity> access_token(String app_key, String app_secret, String grant_type, String account,
        String password, String p_signature, String code, String redirect_uri, String refresh_token);
}
