package com.bigfat.coolweather.util;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/16
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
