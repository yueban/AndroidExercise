package com.yueban.sunflower.utilities

import android.content.Context
import com.yueban.sunflower.data.AppDatabase
import com.yueban.sunflower.data.GardenPlantingRepository
import com.yueban.sunflower.data.PlantRepository
import com.yueban.sunflower.vm.GardenPlantingListVMFactory
import com.yueban.sunflower.vm.PlantDetailVMFactory
import com.yueban.sunflower.vm.PlantListVMFactory

/**
 * @author yueban
 * @date 2018/12/18
 * @email fbzhh007@gmail.com
 */
object Injector {
    private fun getPlantRepository(context: Context): PlantRepository {
        return PlantRepository.getInstance(AppDatabase.getInstance(context).plantDao())
    }

    private fun getGardenPlantingRepository(context: Context): GardenPlantingRepository {
        return GardenPlantingRepository.getInstance(AppDatabase.getInstance(context).gardenPlantingDao())
    }

    fun provideGardenPlantingListVMFactory(context: Context): GardenPlantingListVMFactory {
        return GardenPlantingListVMFactory(getGardenPlantingRepository(context))
    }

    fun providePlantListVMFactory(context: Context): PlantListVMFactory {
        return PlantListVMFactory(getPlantRepository(context))
    }

    fun providePlantDetailVMFactory(context: Context, plantId: String): PlantDetailVMFactory {
        return PlantDetailVMFactory(getPlantRepository(context), getGardenPlantingRepository(context), plantId)
    }
}