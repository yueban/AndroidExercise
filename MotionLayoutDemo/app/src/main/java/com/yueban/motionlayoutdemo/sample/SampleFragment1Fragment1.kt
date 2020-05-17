package com.yueban.motionlayoutdemo.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yueban.motionlayoutdemo.R

class SampleFragment1Fragment1 : Fragment() {

    companion object {
        fun newInstance() = SampleFragment1Fragment1()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_fragment_1_1, container, false)
    }
}