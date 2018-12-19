package com.yueban.sunflower.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

/**
 * @author yueban
 * @date 2018/12/17
 * @email fbzhh007@gmail.com
 */
@Dao
interface GardenPlantingDao {
    @Query("select * from gardenplanting")
    fun getGardenPlantings(): LiveData<List<GardenPlanting>>

    @Query("select * from gardenplanting where id=:id")
    fun getGardenPlating(id: Long): LiveData<GardenPlanting>

    @Query("select * from gardenplanting where plantId=:plantId")
    fun getGardenPlantingByPlant(plantId: String): LiveData<GardenPlanting>

    @Transaction
    @Query("select * from plant")
    fun getPlantAndGardenPlantings(): LiveData<List<PlantAndGardenPlantings>>

    @Insert
    fun insertGardenPlanting(gardenPlanting: GardenPlanting): Long

    @Delete
    fun deleteGardenPlanting(gardenPlanting: GardenPlanting)
}