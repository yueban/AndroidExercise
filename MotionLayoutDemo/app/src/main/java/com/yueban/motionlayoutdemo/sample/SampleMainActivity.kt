package com.yueban.motionlayoutdemo.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yueban.motionlayoutdemo.R
import com.yueban.motionlayoutdemo.startActivity

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020/5/17
 */
class SampleMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sample)
    }

    fun gotoFragment1(view: View) = startActivity(SampleFragment1Activity::class)

    fun gotoFragment2(view: View) = startActivity(SampleFragment2Activity::class)

    fun gotoYoutube(view: View) = startActivity(SampleYoutubeActivity::class)

    fun gotoKeyTrigger(view: View) = startActivity(SampleKeyTriggerActivity::class)

    fun gotoMultiState(view: View) = startActivity(SampleMultistateActivity::class)
}