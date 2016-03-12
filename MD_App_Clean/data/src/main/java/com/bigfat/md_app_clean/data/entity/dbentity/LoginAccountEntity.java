package com.bigfat.md_app_clean.data.entity.dbentity;

import com.bigfat.md_app_clean.data.db.AppDB;
import com.bigfat.md_app_clean.data.entity.viewentity.AuthEntity;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import java.util.Date;

/**
 * 登录账号信息
 *
 * Created by yueban on 17:48 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@ModelContainer
@Table(database = AppDB.class)
public class LoginAccountEntity extends BaseModel {
    @PrimaryKey
    private String account;
    @Column
    private String password;
    @Column
    private Date updateTime;
    @ForeignKey
    private AuthEntity authEntity;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public AuthEntity getAuthEntity() {
        return authEntity;
    }

    public void setAuthEntity(AuthEntity authEntity) {
        this.authEntity = authEntity;
    }

    @Override
    public String toString() {
        return "LoginAccountEntity{" +
            "account='" + account + '\'' +
            ", password='" + password + '\'' +
            ", updateTime=" + updateTime +
            ", authEntity=" + authEntity +
            '}';
    }
}
