package com.yueban.motionlayoutdemo.sample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.yueban.motionlayoutdemo.R

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020/5/17
 */
class SampleFragment1Fragment2 : Fragment() {
    private lateinit var motionLayout: MotionLayout

    companion object {
        fun newInstance() = SampleFragment1Fragment2()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_fragment_1_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        motionLayout = view.findViewById(R.id.fragment_2)
    }

    override fun onStart() {
        super.onStart()
        Log.i(SampleFragment1Fragment2::class.java.simpleName, "onStart of fragment...")
    }
}