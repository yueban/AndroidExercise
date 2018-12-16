package com.yueban.androidkotlindemo.navigation

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.yueban.androidkotlindemo.R
import kotlinx.android.synthetic.main.activity_navigatioin_1.*

/**
 * @author yueban
 * @date 2018/12/14
 * @email fbzhh007@gmail.com
 */
class NavigationActivity1 : AppCompatActivity(), NavigationFragment1.OnFragmentInteractionListener,
    NavigationFragment2.OnFragmentInteractionListener {
    override fun onFragmentInteraction1(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFragmentInteraction2(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigatioin_1)

        /********** demo 2: create NavHostFragment programmatically **********/
//        val host = NavHostFragment.create(R.navigation.nav_graph)
//        supportFragmentManager.beginTransaction().replace(R.id.nav_host_frame_layout, host)
//            .setPrimaryNavigationFragment(host)
//            .commit()

        /********** demo 3 **********/
        findNavController(R.id.nav_host_fragment_navigation)
            .addOnDestinationChangedListener { controller, destination, arguments ->
                Log.d("OnDestinationChanged", "destination: $destination")
                Log.d("OnDestinationChanged", "is navigationFragment2: ${R.id.navigationFragment2 == destination.id}")
//                Log.d("OnDestinationChanged", "arguments: $arguments")
            }

        /********** demo 4: Navigation UI **********/
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment_navigation)
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigationFragment1, R.id.navigationFragment2), drawer)
//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigationFragment1, R.id.nav_include_fragments))
        toolbar.setupWithNavController(navController, appBarConfiguration)
        navigation.setupWithNavController(navController)
        bottom_navigation.setupWithNavController(navController)

        /********** demo 5: add custom Navigator **********/
//        navController.navigatorProvider += CustomNavigator()

        /********** demo 5: conditional navigation **********/
//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            when (destination.id) {
//                R.id.navigationFragment1 -> {
//                    val isLogin = true
//                    if (!isLogin) {
//                        //Navigate to LoginFragment
//                    }
//                }
//            }
//        }
    }

    /********** demo 1: set app:defaultNavHost="true" programmatically **********/
//    override fun onSupportNavigateUp(): Boolean {
//        return findNavController(R.id.nav_host_fragment_navigation).navigateUp()
//    }
}