package com.yueban.sunflower.data

/**
 * @author yueban
 * @date 2018/12/17
 * @email fbzhh007@gmail.com
 */
class PlantRepository private constructor(private val plantDao: PlantDao) {
    fun getPlants() = plantDao.getPlants()

    fun getPlant(id: String) = plantDao.getPlant(id)

    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int) = plantDao.getPlantsWithGrowZoneNumber(growZoneNumber)

    companion object {
        @Volatile
        private var instance: PlantRepository? = null

        fun getInstance(plantDao: PlantDao) =
            instance ?: synchronized(this) {
                instance ?: PlantRepository(plantDao).also { instance = it }
            }
    }
}