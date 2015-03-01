package com.bigfat.arcmenu.view.util;

import android.view.View;

/**
 * ArcMenu点击子菜单项的回调接口
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/1
 */
public interface OnMenuItemClickListener {
    void onClick(View view, int pos);
}