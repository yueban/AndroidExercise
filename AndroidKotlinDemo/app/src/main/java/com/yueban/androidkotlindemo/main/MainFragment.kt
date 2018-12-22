package com.yueban.androidkotlindemo.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.yueban.androidkotlindemo.R
import com.yueban.androidkotlindemo.databinding.DataBindingActivity1
import com.yueban.androidkotlindemo.demo.paging.ui.SearchRepositoriesActivity
import com.yueban.androidkotlindemo.demo.roomwithview.ui.RoomWithViewActivity
import com.yueban.androidkotlindemo.lifecycle.LifecycleActivity1
import com.yueban.androidkotlindemo.lifecycle.LifecycleActivity2
import com.yueban.androidkotlindemo.livedata.LiveDataActivity1
import com.yueban.androidkotlindemo.navigation.NavigationActivity1
import com.yueban.androidkotlindemo.room.RoomActivity1
import com.yueban.androidkotlindemo.viewmodel.ViewModelActivity1
import com.yueban.androidkotlindemo.viewmodel.ViewModelActivity2
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.data.observe(this, Observer { data ->
            goto_end_fragment.text = data
        })

        goto_end_fragment.setOnClickListener { it ->
            it?.let {
                Navigation.findNavController(it).navigate(R.id.end_action)
            }
        }
        goto_lifecycle_activity_1.setOnClickListener {
            startActivity(Intent(activity, LifecycleActivity1::class.java))
        }
        goto_lifecycle_activity_2.setOnClickListener {
            startActivity(Intent(activity, LifecycleActivity2::class.java))
        }
        goto_livedata_activity_1.setOnClickListener {
            startActivity(Intent(activity, LiveDataActivity1::class.java))
        }
        goto_room_activity_1.setOnClickListener {
            startActivity(Intent(activity, RoomActivity1::class.java))
        }
        goto_viewmodel_activity_1.setOnClickListener {
            startActivity(Intent(activity, ViewModelActivity1::class.java))
        }
        goto_viewmodel_activity_2.setOnClickListener {
            startActivity(Intent(activity, ViewModelActivity2::class.java))
        }
        goto_room_with_view_activity.setOnClickListener {
            startActivity(Intent(activity, RoomWithViewActivity::class.java))
        }
        goto_search_repositories_activity.setOnClickListener {
            startActivity(Intent(activity, SearchRepositoriesActivity::class.java))
        }
        goto_navigation_activity_1.setOnClickListener {
            startActivity(Intent(activity, NavigationActivity1::class.java))
        }
        start_navigation_deep_link.setOnClickListener {
            //            val data = Uri.parse("http://www.yueban.com/navigation/1")
//            val intent = Intent(Intent.ACTION_VIEW, data)
//            startActivity(intent)

            /********** demo: create pendingIntent by NavDeepLinkBuilder **********/
//            context?.let { context ->
//                val pendingIntent = NavDeepLinkBuilder(context)
//                    .setGraph(R.navigation.navigation)
//                    .setDestination(R.id.end_dest)
////                .setArguments(args)
//                    .createPendingIntent()
//
//                val channelId = "common"
//                val channelName = "common name"
//                val channelDescription = "common notification"
//
//                // Create the NotificationChannel, but only on API 26+ because
//                // the NotificationChannel class is new and not in the support library
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    val importance = NotificationManager.IMPORTANCE_DEFAULT
//                    val channel = NotificationChannel(channelId, channelName, importance).apply {
//                        description = channelDescription
//                    }
//                    // Register the channel with the system
//                    val notificationManager: NotificationManager =
//                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                    notificationManager.createNotificationChannel(channel)
//                }
//
//                val notification =
//                    NotificationCompat.Builder(context, channelId).setSmallIcon(R.drawable.ic_eye)
//                        .setContentTitle("My notification")
//                        .setContentText("Much longer text that cannot fit one line...")
//                        .setStyle(
//                            NotificationCompat.BigTextStyle()
//                                .bigText("Much longer text that cannot fit one line...")
//                        )
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .setAutoCancel(true)
//                        .setContentIntent(pendingIntent)
//                        .build()
//                with(NotificationManagerCompat.from(context)) {
//                    // notificationId is a unique int for each notification that you must define
//                    notify(0, notification)
//                }
//            }
        }
        start_databinding_activity_1.setOnClickListener {
            startActivity(Intent(activity, DataBindingActivity1::class.java))
        }
    }
}