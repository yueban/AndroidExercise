package com.yueban.motionlayoutdemo.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yueban.motionlayoutdemo.R
import kotlinx.android.synthetic.main.activity_sample_youtube.*

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020/5/17
 */
class SampleYoutubeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_youtube)
        recyclerview_front.apply {
            adapter = FrontPhotosAdapter()
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@SampleYoutubeActivity)
        }
    }
}