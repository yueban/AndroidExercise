package com.yueban.sunflower

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.yueban.sunflower.databinding.FragmentPlantDetailBinding
import com.yueban.sunflower.utilities.Injector
import com.yueban.sunflower.vm.PlantDetailVM

/**
 * @author yueban
 * @date 2018/12/18
 * @email fbzhh007@gmail.com
 */
class PlantDetailFragment : Fragment() {
    private lateinit var mPlantId: String

    private lateinit var mBinding: FragmentPlantDetailBinding
    private lateinit var mVM: PlantDetailVM
    private lateinit var shareText: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mPlantId = PlantDetailFragmentArgs.fromBundle(arguments).plantId

        mBinding = FragmentPlantDetailBinding.inflate(inflater, container, false)

        mBinding.fab.setOnClickListener {
            mVM.addPlantToGarden()
            Snackbar.make(it, R.string.added_plant_to_garden, Snackbar.LENGTH_LONG).show()
        }

        val factory = Injector.providePlantDetailVMFactory(mBinding.root.context, mPlantId)
        mVM = ViewModelProviders.of(this, factory).get(PlantDetailVM::class.java)
        mBinding.viewModel = mVM
        mBinding.setLifecycleOwner(this)

        mVM.plant.observe(this, Observer { plant ->
            shareText = if (plant == null) {
                ""
            } else {
                getString(R.string.share_text_plant, plant.name)
            }
        })

        setHasOptionsMenu(true)

        return mBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_plant_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                val shareIntent = ShareCompat.IntentBuilder.from(activity)
                    .setText(shareText)
                    .setType("text/plain")
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                startActivity(shareIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}