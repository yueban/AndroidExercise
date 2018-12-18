package com.yueban.sunflower.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yueban.sunflower.data.GardenPlantingRepository
import com.yueban.sunflower.data.PlantAndGardenPlantings

/**
 * @author yueban
 * @date 2018/12/18
 * @email fbzhh007@gmail.com
 */
class GardenPlantingListVM internal constructor(
    private val gardenPlantingRepository: GardenPlantingRepository
) :
    ViewModel() {
    val gardenPlantings = gardenPlantingRepository.getGardenPlantings()

    val plantAndGardenPlantings: LiveData<List<PlantAndGardenPlantings>> =
        Transformations.map(gardenPlantingRepository.getPlantAndGardenPlantings()) { plantings ->
            plantings.filter {
                it.gardenPlantings.isNotEmpty()
            }
        }
}