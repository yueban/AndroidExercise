package com.yueban.sunflower.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yueban.sunflower.PlantListFragmentDirections
import com.yueban.sunflower.data.Plant
import com.yueban.sunflower.databinding.ItemPlantBinding

/**
 * @author yueban
 * @date 2018/12/18
 * @email fbzhh007@gmail.com
 */
class PlantListAdapter : ListAdapter<Plant, PlantListAdapter.ViewHolder>(PlantDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plant = getItem(position)
        holder.apply {
            itemView.tag = plant
            bind(createOnClickListener(plant.id), plant)
        }
    }

    private fun createOnClickListener(plantId: String): View.OnClickListener {
        return View.OnClickListener {
            val direction = PlantListFragmentDirections.actionPlantListFragmentToPlantDetailFragment(plantId)
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder(private val binding: ItemPlantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, plant: Plant) {
            binding.apply {
                clickListener = listener
                this.plant = plant
                executePendingBindings()
            }
        }
    }
}