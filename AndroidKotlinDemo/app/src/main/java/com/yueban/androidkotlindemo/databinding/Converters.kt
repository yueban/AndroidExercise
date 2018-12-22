package com.yueban.androidkotlindemo.databinding

import android.annotation.SuppressLint
import androidx.databinding.InverseMethod
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @author yueban
 * @date 2018/12/22
 * @email fbzhh007@gmail.com
 */
object Converters {
    @SuppressLint("ConstantLocale")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    @JvmStatic
    @InverseMethod("stringToDate")
    fun dateToString(value: Long): CharSequence {
        return dateFormat.format(Date(value))
    }

    @JvmStatic
    fun stringToDate(value: CharSequence): Long {
        return dateFormat.parse(value.toString()).time
    }
}