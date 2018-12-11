package com.yueban.androidkotlindemo.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**
 * @author yueban
 * @date 2018/12/5
 * @email fbzhh007@gmail.com
 */

class LiveDataViewModel2(repository: PostalCodeRepository) : ViewModel() {
    private val addressInput = MutableLiveData<String>()
    val postalCode: LiveData<String> = Transformations.switchMap(addressInput) { address ->
        repository.getPostCode(address)
    }

    public fun setInput(address: String) {
        addressInput.value = address
    }
}