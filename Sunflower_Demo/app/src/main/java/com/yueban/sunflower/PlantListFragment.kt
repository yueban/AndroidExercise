package com.yueban.sunflower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yueban.sunflower.adapter.PlantListAdapter
import com.yueban.sunflower.databinding.FragmentPlantListBinding
import com.yueban.sunflower.utilities.Injector
import com.yueban.sunflower.vm.PlantListVM
import com.yueban.sunflower.vm.PlantListVMFactory

/**
 * @author yueban
 * @date 2018/12/18
 * @email fbzhh007@gmail.com
 */
class PlantListFragment : Fragment() {
    private lateinit var mBinding: FragmentPlantListBinding
    private lateinit var mAdapter: PlantListAdapter
    private lateinit var mVM: PlantListVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentPlantListBinding.inflate(inflater, container, false)

        mAdapter = PlantListAdapter()
        val factory: PlantListVMFactory = Injector.providePlantListVMFactory(mBinding.root.context)
        mVM = ViewModelProviders.of(this, factory).get(PlantListVM::class.java)

        mBinding.plantList.adapter = mAdapter
        mVM.getPlants().observe(viewLifecycleOwner, Observer { plants ->
            plants?.apply {
                mAdapter.submitList(this)
            }
        })

        setHasOptionsMenu(true)

        return mBinding.root
    }

    private fun updateData() {
        with(mVM) {
            if (isFiltered()) {
                clearGrowZoneNumber()
            } else {
                setGrowZoneNumber(8)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_plant_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_zone -> {
                updateData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}