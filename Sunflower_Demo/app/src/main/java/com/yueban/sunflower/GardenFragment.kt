package com.yueban.sunflower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yueban.sunflower.adapter.GardenPlantingAdapter
import com.yueban.sunflower.databinding.FragmentGardenBinding
import com.yueban.sunflower.utilities.Injector
import com.yueban.sunflower.vm.GardenPlantingListVM

/**
 * @author yueban
 * @date 2018/12/18
 * @email fbzhh007@gmail.com
 */
class GardenFragment : Fragment() {
    private lateinit var mBinding: FragmentGardenBinding
    private lateinit var mAdapter: GardenPlantingAdapter
    private lateinit var mVM: GardenPlantingListVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentGardenBinding.inflate(inflater, container, false)

        mAdapter = GardenPlantingAdapter()
        mBinding.gardenList.adapter = mAdapter

        val factory = Injector.provideGardenPlantingListVMFactory(mBinding.root.context)
        mVM = ViewModelProviders.of(this, factory).get(GardenPlantingListVM::class.java)
        mVM.gardenPlantings.observe(viewLifecycleOwner, Observer { plantings ->
            mBinding.hasPlantings = !plantings.isNullOrEmpty()
        })
        mVM.plantAndGardenPlantings.observe(viewLifecycleOwner, Observer { plantings ->
            if (!plantings.isNullOrEmpty()) {
                mAdapter.submitList(plantings)
            }
        })

        return mBinding.root
    }
}