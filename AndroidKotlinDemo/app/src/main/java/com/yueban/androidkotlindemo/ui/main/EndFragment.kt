package com.yueban.androidkotlindemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yueban.androidkotlindemo.R
import kotlinx.android.synthetic.main.fragment_end.*

/**
 * @author yueban
 * @date 2018/11/6
 * @email fbzhh007@gmail.com
 */

class EndFragment : Fragment() {
    companion object {
        fun newInstance() = EndFragment()
    }

    private lateinit var viewModel: EndViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_end, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EndViewModel::class.java)
        viewModel.data.observe(this, Observer { data ->
            text_end.text = data
        })
    }
}