package com.yueban.motionlayoutdemo.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yueban.motionlayoutdemo.R

class SampleFragment2ItemFragment : Fragment() {

    companion object {
        fun newInstance() = SampleFragment2ItemFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.item_fragment_sample_fragment_2_layout, container, false)
    }

    private lateinit var myHolder: CustomAdapter.ViewHolder

    fun update(holder: CustomAdapter.ViewHolder) {
        myHolder = holder
        view?.findViewById<TextView>(R.id.txtTitle)?.text = holder.txtTitle.text
        view?.findViewById<TextView>(R.id.txtName)?.text = holder.txtName.text
    }

    override fun onStart() {
        super.onStart()
        if (this::myHolder.isInitialized) {
            update(myHolder)
        }
    }
}