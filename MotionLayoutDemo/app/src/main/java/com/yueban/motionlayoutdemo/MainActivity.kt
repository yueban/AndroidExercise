package com.yueban.motionlayoutdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yueban.motionlayoutdemo.codelab.CodeLabMainActivity
import com.yueban.motionlayoutdemo.medium.MediumMainActivity
import com.yueban.motionlayoutdemo.sample.SampleMainActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun gotoCodeLabDemo(view: View) = gotoView(CodeLabMainActivity::class.java)

    fun gotoMediumDemo(view: View) = gotoView(MediumMainActivity::class.java)

    fun gotoGoogleSampleDemo(view: View) = gotoView(SampleMainActivity::class.java)

    private fun gotoView(clazz: Class<out Activity>) {
        startActivity(Intent(this, clazz))
    }
}
