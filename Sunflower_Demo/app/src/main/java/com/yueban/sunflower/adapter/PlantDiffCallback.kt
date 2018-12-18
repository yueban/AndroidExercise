package com.yueban.sunflower.adapter

import androidx.recyclerview.widget.DiffUtil
import com.yueban.sunflower.data.Plant

/**
 * @author yueban
 * @date 2018/12/18
 * @email fbzhh007@gmail.com
 */
class PlantDiffCallback : DiffUtil.ItemCallback<Plant>() {
    override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
        return oldItem == newItem
    }
}