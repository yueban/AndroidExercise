package com.bigfat.md_app_clean.data.exception;

/**
 * 网络连接错误
 *
 * Created by yueban on 15:37 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class NetworkConnectionException extends Exception {
    public NetworkConnectionException() {
        super();
    }

    public NetworkConnectionException(final String message) {
        super(message);
    }

    public NetworkConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NetworkConnectionException(final Throwable cause) {
        super(cause);
    }
}
