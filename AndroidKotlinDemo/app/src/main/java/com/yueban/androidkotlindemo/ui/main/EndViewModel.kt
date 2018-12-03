package com.yueban.androidkotlindemo.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author yueban
 * @date 2018/11/6
 * @email fbzhh007@gmail.com
 */
class EndViewModel : ViewModel() {
    private val _data = MutableLiveData<String>()

    val data: LiveData<String>
        get() = _data

    init {
        _data.value = "Good Bye!"
    }
}