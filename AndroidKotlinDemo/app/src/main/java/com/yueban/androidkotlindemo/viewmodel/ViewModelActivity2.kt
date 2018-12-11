package com.yueban.androidkotlindemo.viewmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yueban.androidkotlindemo.R

/**
 * @author yueban
 * @date 2018/12/8
 * @email fbzhh007@gmail.com
 */
class ViewModelActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model_2)

        supportFragmentManager.beginTransaction().add(R.id.frame1, MasterFragment.newInstance()).commit()
        supportFragmentManager.beginTransaction().add(R.id.frame2, DetailFragment.newInstance()).commit()
    }
}