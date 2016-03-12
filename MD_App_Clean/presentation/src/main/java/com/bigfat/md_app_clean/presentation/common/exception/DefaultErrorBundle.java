package com.bigfat.md_app_clean.presentation.common.exception;

/**
 * Created by yueban on 12:33 2/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class DefaultErrorBundle implements ErrorBundle {
    private static final String DEFAULT_ERROR_MSG = "Unknown error";
    private final Exception mException;

    public DefaultErrorBundle(Exception exception) {
        mException = exception;
    }

    public DefaultErrorBundle(Throwable throwable) {
        mException = new Exception(throwable);
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
