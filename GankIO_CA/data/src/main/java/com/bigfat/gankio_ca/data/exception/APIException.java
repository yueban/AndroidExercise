package com.bigfat.gankio_ca.data.exception;

/**
 * Created by yueban on 15:37 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class APIException extends Exception {
    public APIException() {
        super();
    }

    public APIException(final String message) {
        super(message);
    }

    public APIException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public APIException(final Throwable cause) {
        super(cause);
    }
}
