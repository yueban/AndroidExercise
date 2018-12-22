package com.yueban.androidkotlindemo.databinding

import android.widget.TextView
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods

/**
 * @author yueban
 * @date 2018/12/22
 * @email fbzhh007@gmail.com
 */
@BindingMethods(
    value = [
        BindingMethod(
            type = TextView::class,
            attribute = "android:textColorHint",
            method = "setTextColor"
        )
    ]
)
class BindingMethods