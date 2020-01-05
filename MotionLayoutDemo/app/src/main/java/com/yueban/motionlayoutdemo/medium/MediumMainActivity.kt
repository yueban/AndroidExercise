package com.yueban.motionlayoutdemo.medium

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yueban.motionlayoutdemo.R

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-05
 */
class MediumMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_medium)
    }

    fun gotoPart2Ex4(view: View) = gotoView(MediumPart2Ex4Activity::class.java)

    fun gotoPart2Ex5(view: View) = gotoView(MediumPart2Ex5Activity::class.java)

    private fun gotoView(clazz: Class<out Activity>) {
        startActivity(Intent(this, clazz))
    }
}