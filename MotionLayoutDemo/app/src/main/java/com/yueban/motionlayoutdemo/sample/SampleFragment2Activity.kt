package com.yueban.motionlayoutdemo.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.yueban.motionlayoutdemo.R
import kotlinx.android.synthetic.main.activity_sample_fragment.*
import kotlin.math.abs

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020/5/17
 */
class SampleFragment2Activity : AppCompatActivity(), MotionLayout.TransitionListener {
    private lateinit var fragment: Fragment
    private var lastProgress = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_fragment)

        if (savedInstanceState == null) {
            fragment = SampleFragment1Fragment1.newInstance().also {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, it)
                    .commitNow()
            }
        }
        motion_layout.setTransitionListener(this)
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        if (p3 - lastProgress > 0) {
            // from start to end
            val atEnd = abs(p3 - 1f) < 0.1f
            if (atEnd && fragment is SampleFragment1Fragment1) {
                fragment = SampleFragment2ListFragment.newInstance().also {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.show, 0)
                        .replace(R.id.container, it)
                        .commitNow()
                }
            }
        } else {
            // from end to start
            val atStart = p3 < 0.9f
            if (atStart && fragment is SampleFragment2ListFragment) {
                fragment = SampleFragment1Fragment1.newInstance().also {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(0, R.animator.hide)
                        .replace(R.id.container, it)
                        .commitNow()
                }
            }
        }
        lastProgress = p3
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
    }
}