package com.yueban.motionlayoutdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yueban.motionlayoutdemo.codelab.CodeLabMainActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun gotoCodeLabDemo(view: View) = gotoView(CodeLabMainActivity::class.java)

    private fun gotoView(clazz: Class<out Activity>) {
        startActivity(Intent(this, clazz))
    }
}
