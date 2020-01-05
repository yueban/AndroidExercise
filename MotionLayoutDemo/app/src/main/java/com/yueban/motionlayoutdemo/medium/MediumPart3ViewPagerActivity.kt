package com.yueban.motionlayoutdemo.medium

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.yueban.motionlayoutdemo.R

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-05
 */
class MediumPart3ViewPagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medium_part_3_view_pager)

        val header: ViewPagerHeader = findViewById(R.id.pager_header)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = object : PagerAdapter() {
            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val textView = TextView(container.context)
                val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                textView.layoutParams = params
                textView.gravity = Gravity.CENTER
                textView.text = "page_$position"
                container.addView(textView)
                return textView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View?)
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

            override fun getCount() = 3
        }
        viewPager.addOnPageChangeListener(header)
    }
}