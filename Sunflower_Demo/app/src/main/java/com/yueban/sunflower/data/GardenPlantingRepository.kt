package com.yueban.sunflower.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author yueban
 * @date 2018/12/17
 * @email fbzhh007@gmail.com
 */
class GardenPlantingRepository private constructor(
    private val gardenPlantingDao: GardenPlantingDao
) {
    suspend fun createGardenPlating(plantId: String) {
        withContext(Dispatchers.IO) {
            val gardenPlanting = GardenPlanting(plantId)
            gardenPlantingDao.insertGardenPlanting(gardenPlanting)
        }
    }

    suspend fun removeGardenPlanting(gardenPlanting: GardenPlanting) {
        withContext(Dispatchers.IO) {
            gardenPlantingDao.deleteGardenPlanting(gardenPlanting)
        }
    }

    fun getGardenPlantingByPlant(plantId: String) = gardenPlantingDao.getGardenPlantingByPlant(plantId)

    fun getGardenPlantings() = gardenPlantingDao.getGardenPlantings()

    fun getPlantAndGardenPlantings() = gardenPlantingDao.getPlantAndGardenPlantings()

    companion object {
        @Volatile
        private var instance: GardenPlantingRepository? = null

        fun getInstance(gardenPlantingDao: GardenPlantingDao) =
            instance ?: synchronized(this) {
                instance ?: GardenPlantingRepository(gardenPlantingDao).also { instance = it }
            }
    }
}