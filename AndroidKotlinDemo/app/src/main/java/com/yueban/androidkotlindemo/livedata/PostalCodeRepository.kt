package com.yueban.androidkotlindemo.livedata

import androidx.lifecycle.LiveData

/**
 * @author yueban
 * @date 2018/12/5
 * @email fbzhh007@gmail.com
 */

class PostalCodeRepository {
    fun getPostCode(address: String): LiveData<String> {
        // mock
        val a = object : LiveData<String>() {
            public override fun setValue(value: String?) {
                super.setValue(value)
            }
        }
        a.value = "postcode: $address"
        return a
    }
}