package com.bigfat.md_app_clean.presentation.ui.login.presenter;

import com.bigfat.md_app_clean.presentation.common.Presenter;
import com.bigfat.md_app_clean.presentation.ui.login.view.LoginView;

/**
 * Created by yueban on 16:43 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface LoginPresenter extends Presenter<LoginView> {
    void login(String account, String password);
}
