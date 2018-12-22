package com.yueban.androidkotlindemo.databinding

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author yueban
 * @date 2018/12/22
 * @email fbzhh007@gmail.com
 */
class CustomTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    var time: Int = 0
        set(value) {
            field = value
            onTimeChangeListener?.onTimeChanged(value)
        }

    private var onTimeChangeListener: OnTimeChangeListener? = null

    fun setOnTimeChangeListener(onTimeChangeListener: OnTimeChangeListener?) {
        this.onTimeChangeListener = onTimeChangeListener
    }

    interface OnTimeChangeListener {
        fun onTimeChanged(time: Int)
    }
}