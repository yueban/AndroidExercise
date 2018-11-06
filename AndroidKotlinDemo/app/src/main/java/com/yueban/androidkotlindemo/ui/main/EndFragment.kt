package com.yueban.androidkotlindemo.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yueban.androidkotlindemo.R

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
        return inflater.inflate(R.layout.end_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EndViewModel::class.java)
        viewModel.data.observe(this, Observer { data ->
            view?.findViewById<TextView>(R.id.text_end)?.text = data
        })
    }
}