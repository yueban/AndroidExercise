package com.bigfat.md_app_clean.data.exception;

/**
 * API错误
 *
 * Created by yueban on 15:37 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class APIException extends RuntimeException {
    private ApiErrorModel mApiErrorModel;

    public APIException() {
        super();
    }

    public APIException(ApiErrorModel apiErrorModel) {
        mApiErrorModel = apiErrorModel;
    }

    public ApiErrorModel getApiErrorModel() {
        return mApiErrorModel;
    }
}
