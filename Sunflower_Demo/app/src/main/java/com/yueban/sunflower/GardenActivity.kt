package com.yueban.sunflower

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.yueban.sunflower.databinding.ActivityGardenBinding

class GardenActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityGardenBinding
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_garden)

        mDrawerLayout = mBinding.drawerLayout
        mNavController = Navigation.findNavController(this, R.id.garden_nav_fragment)
        mAppBarConfiguration = AppBarConfiguration(mNavController.graph, mDrawerLayout)
        setSupportActionBar(mBinding.toolbar)
        setupActionBarWithNavController(mNavController, mAppBarConfiguration)
        mBinding.navigationView.setupWithNavController(mNavController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp(mAppBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
