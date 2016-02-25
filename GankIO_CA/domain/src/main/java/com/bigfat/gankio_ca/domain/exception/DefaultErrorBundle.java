package com.bigfat.gankio_ca.domain.exception;

/**
 * Exception封装类
 * Created by yueban on 11:23 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class DefaultErrorBundle implements ErrorBundle {
    private static final String DEFAULT_ERROR_MSG = "Unknown error";
    private final Exception mException;

    public DefaultErrorBundle(Exception exception) {
        mException = exception;
    }

    @Override
    public Exception getException() {
        return mException;
    }

    @Override
    public String getErrorMessage() {
        return (mException != null) ? mException.getMessage() : DEFAULT_ERROR_MSG;
    }
}
