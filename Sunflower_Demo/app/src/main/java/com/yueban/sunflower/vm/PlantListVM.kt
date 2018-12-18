package com.yueban.sunflower.vm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yueban.sunflower.data.Plant
import com.yueban.sunflower.data.PlantRepository

/**
 * @author yueban
 * @date 2018/12/18
 * @email fbzhh007@gmail.com
 */
class PlantListVM internal constructor(
    private val plantRepository: PlantRepository
) : ViewModel() {
    private val growZoneNumber = MutableLiveData<Int>()
    private val plantList = MediatorLiveData<List<Plant>>()

    init {
        growZoneNumber.value = NO_GROW_ZONE

        val livePlantList = Transformations.switchMap(growZoneNumber) {
            if (it == NO_GROW_ZONE) {
                plantRepository.getPlants()
            } else {
                plantRepository.getPlantsWithGrowZoneNumber(it)
            }
        }
        plantList.addSource(livePlantList, plantList::setValue)
    }

    fun getPlants() = plantList

    fun setGrowZoneNumber(num: Int) {
        growZoneNumber.value = num
    }

    fun clearGrowZoneNumber() {
        growZoneNumber.value = NO_GROW_ZONE
    }

    fun isFiltered() = growZoneNumber.value != NO_GROW_ZONE

    companion object {
        private const val NO_GROW_ZONE = -1
    }
}