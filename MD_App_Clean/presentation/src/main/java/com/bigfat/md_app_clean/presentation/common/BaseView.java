package com.bigfat.md_app_clean.presentation.common;

import android.content.Context;

/**
 * Created by yueban on 16:03 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface BaseView {
    void showLoading();

    void hideLoading();

    void showToast(String message);

    Context getContext();
}
