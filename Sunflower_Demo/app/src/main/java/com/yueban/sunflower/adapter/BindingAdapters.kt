package com.yueban.sunflower.adapter

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * @author yueban
 * @date 2018/12/18
 * @email fbzhh007@gmail.com
 */
@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}