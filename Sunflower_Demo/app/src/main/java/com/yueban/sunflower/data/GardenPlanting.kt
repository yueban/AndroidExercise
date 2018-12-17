package com.yueban.sunflower.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

/**
 * @author yueban
 * @date 2018/12/17
 * @email fbzhh007@gmail.com
 */
@Entity(
    foreignKeys = [ForeignKey(entity = Plant::class, parentColumns = ["id"], childColumns = ["plantId"])],
    indices = [Index("plantId")]
)
data class GardenPlanting(
    val plantId: String,
    val plantDate: Calendar = Calendar.getInstance(),
    val lastWateringDate: Calendar = Calendar.getInstance()
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}