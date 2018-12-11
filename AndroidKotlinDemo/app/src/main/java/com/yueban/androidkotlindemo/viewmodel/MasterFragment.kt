package com.yueban.androidkotlindemo.viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.yueban.androidkotlindemo.R
import kotlinx.android.synthetic.main.fragment_master.*

/**
 * @author yueban
 * @date 2018/12/8
 * @email fbzhh007@gmail.com
 */
class MasterFragment : Fragment() {
    companion object {
        fun newInstance() = MasterFragment()
    }

    private lateinit var model: SharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_master, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = activity?.run { ViewModelProviders.of(this).get(SharedViewModel::class.java) } ?:
            throw Exception("Invalid Activity")

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            model.select(checkedId)
        }
    }
}