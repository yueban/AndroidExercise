package com.bigfat.md_app_clean.presentation.common.navigator;

import android.content.Context;
import com.bigfat.md_app_clean.presentation.ui.login.LoginActivity;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 导航
 *
 * Created by yueban on 15:42 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class Navigator {
    @Inject
    public Navigator() {
    }

    /**
     * 启动登录界面
     */
    public void navigateLogin(Context context) {
        context.startActivity(LoginActivity.getCallingIntent(context));
    }
}
