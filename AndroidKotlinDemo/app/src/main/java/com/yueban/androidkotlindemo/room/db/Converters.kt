package com.yueban.androidkotlindemo.room.db

import androidx.room.TypeConverter
import java.util.Date

/**
 * @author yueban
 * @date 2018/12/7
 * @email fbzhh007@gmail.com
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}