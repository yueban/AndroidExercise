package com.bigfat.networkbestpractice;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/14
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
