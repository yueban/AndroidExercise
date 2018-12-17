package com.yueban.sunflower.data

import androidx.room.TypeConverter
import java.util.Calendar

/**
 * @author yueban
 * @date 2018/12/17
 * @email fbzhh007@gmail.com
 */
class Converters {
    @TypeConverter
    fun calendarToDateStamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun dateStampToCalendar(value: Long): Calendar = Calendar.getInstance().apply {
        timeInMillis = value
    }
}