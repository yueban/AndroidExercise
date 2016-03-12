package com.bigfat.md_app_clean.data.exception;

/**
 * Created by yueban on 13:31 2/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class ApiErrorModel {
    private String error_code;
    private String error_msg;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
