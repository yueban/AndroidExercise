package com.bigfat.gankio_ca.data.entity;

/**
 * Created by yueban on 17:58 23/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class BaseEntity {
    private boolean error;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
            "error=" + error +
            '}';
    }
}
