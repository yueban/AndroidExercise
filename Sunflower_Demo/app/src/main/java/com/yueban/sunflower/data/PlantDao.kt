package com.yueban.sunflower.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @author yueban
 * @date 2018/12/17
 * @email fbzhh007@gmail.com
 */
@Dao
interface PlantDao {
    @Query("select * from plant order by name")
    fun getPlants(): LiveData<List<Plant>>

    @Query("select * from plant where growZoneNumber = :growZoneNumber order by name")
    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int): LiveData<List<Plant>>

    @Query("select * from plant where id = :id")
    fun getPlant(id: String): LiveData<Plant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(plants: List<Plant>)
}