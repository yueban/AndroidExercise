package com.yueban.sunflower.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

/**
 * @author yueban
 * @date 2018/12/17
 * @email fbzhh007@gmail.com
 */
@Entity
data class Plant(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val growZoneNumber: Int,
    val wateringInterval: Int = 7,
    val imageUrl: String = ""
) {
    fun shouldBeWatered(since: Calendar, lastWateringDate: Calendar) = since > lastWateringDate.apply {
        add(Calendar.DAY_OF_YEAR, wateringInterval)
    }

    override fun toString(): String = name
}