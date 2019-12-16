package com.yueban.motionlayoutdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun gotoCodeLab1View(view: View) = gotoView(CodeLab1Activity::class.java)

    fun gotoCodeLab2View(view: View) = gotoView(CodeLab2Activity::class.java)

    fun gotoCodeLab3View(view: View) = gotoView(CodeLab3Activity::class.java)

    fun gotoCodeLab4View(view: View) = gotoView(CodeLab4Activity::class.java)

    fun gotoCodeLab5View(view: View) = gotoView(CodeLab5Activity::class.java)

    fun gotoCodeLab6View(view: View) = gotoView(CodeLab6Activity::class.java)

    fun gotoCodeLab7View(view: View) = gotoView(CodeLab7Activity::class.java)

    fun gotoCodeLab8View(view: View) = gotoView(CodeLab8Activity::class.java)

    private fun gotoView(clazz: Class<out Activity>) {
        startActivity(Intent(this, clazz))
    }
}
