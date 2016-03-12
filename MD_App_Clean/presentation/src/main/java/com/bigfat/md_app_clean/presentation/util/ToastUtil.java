package com.bigfat.md_app_clean.presentation.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;
import com.bigfat.md_app_clean.presentation.common.di.UtilScope;
import javax.inject.Inject;

/**
 * Created by yueban on 12:33 2/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@UtilScope
public class ToastUtil {
    private final Context mContext;
    private Toast mToast;

    @SuppressLint("ShowToast")
    @Inject
    public ToastUtil(Context context) {
        mContext = context;
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    public void showSingleton(String message) {
        mToast.setText(message);
        mToast.show();
    }

    public void showSingleton(int resId) {
        mToast.setText(resId);
        mToast.show();
    }

    public void show(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void show(int resId) {
        Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
    }
}
