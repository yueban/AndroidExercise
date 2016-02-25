package com.bigfat.gankio_ca.presentation.common.ui;

import android.content.Context;

/**
 * Created by yueban on 17:22 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface LoadDataView {
    void showLoading();

    void hideLoading();

    void showRetry();

    void hideRetry();

    void showError(String message);

    Context getContext();
}
