package com.yueban.androidkotlindemo.ui.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author yueban
 * @date 2018/12/5
 * @email fbzhh007@gmail.com
 */
class LiveDataViewModel1 : ViewModel() {
    val message: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}