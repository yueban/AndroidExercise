package com.yueban.androidkotlindemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author yueban
 * @date 2018/12/8
 * @email fbzhh007@gmail.com
 */
class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<Int>()

    fun select(item: Int) {
        selected.value = item
    }
}