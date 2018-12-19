package com.yueban.sunflower.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yueban.sunflower.GardenFragmentDirections
import com.yueban.sunflower.data.PlantAndGardenPlantings
import com.yueban.sunflower.databinding.ItemGardenPlantingBinding
import com.yueban.sunflower.vm.PlantAndGardenPlantingVM

/**
 * @author yueban
 * @date 2018/12/18
 * @email fbzhh007@gmail.com
 */
class GardenPlantingAdapter :
    ListAdapter<PlantAndGardenPlantings, GardenPlantingAdapter.ViewHolder>(GardenPlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGardenPlantingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { plantings ->
            with(holder) {
                itemView.tag = plantings
                bind(createOnClickListener(plantings.plant.id), plantings)
            }
        }
    }

    private fun createOnClickListener(plantId: String): View.OnClickListener {
        return View.OnClickListener {
            val direction = GardenFragmentDirections.actionGardenFragmentToPlantDetailFragment(plantId)
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder(private val binding: ItemGardenPlantingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, plantings: PlantAndGardenPlantings) {
            with(binding) {
                clickListener = listener
                viewModel = PlantAndGardenPlantingVM(
                    itemView.context, plantings
                )
                executePendingBindings()
            }
        }
    }
}