package com.bigfat.md_app_clean.presentation.ui.login.view;

import com.bigfat.md_app_clean.data.entity.viewentity.AuthEntity;
import com.bigfat.md_app_clean.presentation.common.BaseView;

/**
 * Created by yueban on 16:44 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface LoginView extends BaseView {
    void onLoginButtonClick();

    void loginSuccess(AuthEntity authEntity);

    void setAccount(String account);
}
