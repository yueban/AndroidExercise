package com.yueban.androidkotlindemo.ui.viewmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yueban.androidkotlindemo.R

/**
 * @author yueban
 * @date 2018/12/8
 * @email fbzhh007@gmail.com
 */
class ViewModelActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model_1)

        /********** demo 1 **********/
        val model = ViewModelProviders.of(this).get(ViewModel1::class.java)
        model.getUsers().observe(this, Observer { users ->
            for (user in users) {
                println("${user.lastName} ${user.firstName}")
            }
        })
    }
}