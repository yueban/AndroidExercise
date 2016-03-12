package com.bigfat.md_app_clean.presentation.common;

import com.bigfat.md_app_clean.presentation.common.exception.ErrorBundle;

/**
 * Created by yueban on 16:03 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface Presenter<T extends BaseView> {
    void setView(T t);

    void resume();

    void pause();

    void destroy();

    void showErrorMessage(ErrorBundle errorBundle);
}
