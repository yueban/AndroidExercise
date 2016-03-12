package com.bigfat.md_app_clean.data.entity.viewentity;

import android.text.TextUtils;
import com.bigfat.md_app_clean.data.db.AppDB;
import com.bigfat.md_app_clean.data.entity.IsNull;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 登录授权
 *
 * Created by yueban on 17:27 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Table(database = AppDB.class)
public class AuthEntity extends BaseModel implements IsNull {
    /**
     * access_token : D3KJ89YI4QYNJKA
     * expires_in : 1234
     * refresh_token : C3KJ89YI4QYNJDD
     */
    @PrimaryKey
    private String access_token;
    @Column
    private String expires_in;
    @Column
    private String refresh_token;
    @Column
    private String sessionID;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public String toString() {
        return "AuthEntity{" +
            "access_token='" + access_token + '\'' +
            ", expires_in='" + expires_in + '\'' +
            ", refresh_token='" + refresh_token + '\'' +
            ", sessionID='" + sessionID + '\'' +
            '}';
    }

    public boolean isNull() {
        return TextUtils.isEmpty(access_token);
    }
}
