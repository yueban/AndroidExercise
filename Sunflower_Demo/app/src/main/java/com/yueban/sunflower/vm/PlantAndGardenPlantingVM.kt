package com.yueban.sunflower.vm

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yueban.sunflower.R
import com.yueban.sunflower.data.PlantAndGardenPlantings
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * @author yueban
 * @date 2018/12/18
 * @email fbzhh007@gmail.com
 */
class PlantAndGardenPlantingVM internal constructor(
    private val context: Context,
    private val plantings: PlantAndGardenPlantings
) :
    ViewModel() {
    private val dateFormat by lazy { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }

    private val plant = checkNotNull(plantings.plant)
    private val gardenPlanting = plantings.gardenPlantings[0]

    private val plantDateString by lazy { dateFormat.format(gardenPlanting.plantDate.time) }
    private val waterDateString by lazy { dateFormat.format(gardenPlanting.lastWateringDate.time) }
    private val wateringPrefix by lazy {
        context.getString(R.string.watering_next_prefix, waterDateString)
    }
    private val wateringSuffix by lazy {
        context.resources.getQuantityString(
            R.plurals.watering_next_suffix,
            plant.wateringInterval,
            plant.wateringInterval
        )
    }
    // dataBinding
    val imageUrl = ObservableField<String>(plant.imageUrl)
    val plantDate = ObservableField<String>(context.getString(R.string.planted_date, plant.name, plantDateString))
    val waterDate = ObservableField<String>("$wateringPrefix - $wateringSuffix")
}