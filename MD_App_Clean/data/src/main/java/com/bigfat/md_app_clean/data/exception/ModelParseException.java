package com.bigfat.md_app_clean.data.exception;

/**
 * API错误
 *
 * Created by yueban on 15:37 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class ModelParseException extends RuntimeException {
    public ModelParseException() {
        super();
    }

    public ModelParseException(final String message) {
        super(message);
    }

    public ModelParseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ModelParseException(final Throwable cause) {
        super(cause);
    }
}
