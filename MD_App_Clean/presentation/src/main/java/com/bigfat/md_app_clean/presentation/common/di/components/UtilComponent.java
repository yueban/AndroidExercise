package com.bigfat.md_app_clean.presentation.common.di.components;

import com.bigfat.md_app_clean.presentation.common.di.UtilScope;
import com.bigfat.md_app_clean.presentation.util.ToastUtil;
import dagger.Component;

/**
 * Created by yueban on 12:38 2/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@UtilScope
@Component(dependencies = ApplicationComponent.class)
public interface UtilComponent {
    ToastUtil toastUtil();
}
