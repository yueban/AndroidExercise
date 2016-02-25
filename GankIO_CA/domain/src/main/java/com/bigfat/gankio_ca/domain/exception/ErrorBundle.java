package com.bigfat.gankio_ca.domain.exception;

/**
 * Exception封装基类
 * Created by yueban on 11:22 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface ErrorBundle {
    Exception getException();

    String getErrorMessage();
}
