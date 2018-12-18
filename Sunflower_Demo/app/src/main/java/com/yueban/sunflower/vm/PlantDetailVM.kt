package com.yueban.sunflower.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yueban.sunflower.data.GardenPlantingRepository
import com.yueban.sunflower.data.Plant
import com.yueban.sunflower.data.PlantRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author yueban
 * @date 2018/12/18
 * @email fbzhh007@gmail.com
 */
class PlantDetailVM internal constructor(
    private val plantRepository: PlantRepository,
    private val gardenPlantingRepository: GardenPlantingRepository,
    private val plantId: String
) : ViewModel() {
    val isPlanted: LiveData<Boolean>
    val plant: LiveData<Plant>

    init {
        val gardenPlantingByPlant = gardenPlantingRepository.getGardenPlantingByPlant(plantId)
        isPlanted = Transformations.map(gardenPlantingByPlant) {
            it != null
        }
        plant = plantRepository.getPlant(plantId)
    }

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun addPlantToGarden() {
        scope.launch { gardenPlantingRepository.createGardenPlating(plantId) }
    }
}