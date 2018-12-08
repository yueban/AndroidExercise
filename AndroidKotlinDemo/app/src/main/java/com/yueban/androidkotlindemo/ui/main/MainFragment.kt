package com.yueban.androidkotlindemo.ui.main

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
import com.yueban.androidkotlindemo.ui.lifecycle.LifecycleActivity1
import com.yueban.androidkotlindemo.ui.lifecycle.LifecycleActivity2
import com.yueban.androidkotlindemo.ui.livedata.LiveDataActivity1
import com.yueban.androidkotlindemo.ui.room.RoomActivity1
import com.yueban.androidkotlindemo.ui.viewmodel.ViewModelActivity1
import com.yueban.androidkotlindemo.ui.viewmodel.ViewModelActivity2
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
    }
}
