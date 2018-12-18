package com.yueban.sunflower.adapter

import androidx.recyclerview.widget.DiffUtil
import com.yueban.sunflower.data.PlantAndGardenPlantings

/**
 * @author yueban
 * @date 2018/12/18
 * @email fbzhh007@gmail.com
 */
class GardenPlantDiffCallback : DiffUtil.ItemCallback<PlantAndGardenPlantings>() {
    override fun areItemsTheSame(oldItem: PlantAndGardenPlantings, newItem: PlantAndGardenPlantings): Boolean {
        return oldItem.plant.id == newItem.plant.id
    }

    override fun areContentsTheSame(oldItem: PlantAndGardenPlantings, newItem: PlantAndGardenPlantings): Boolean {
        return oldItem == newItem
    }
}