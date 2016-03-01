package com.bigfat.gankio_ca.presentation.common.listeners;

import android.view.View;

/**
 * Created by yueban on 12:22 25/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface OnItemLongClickListener<T> {
    void onLongClick(View view, T t, int position);
}
