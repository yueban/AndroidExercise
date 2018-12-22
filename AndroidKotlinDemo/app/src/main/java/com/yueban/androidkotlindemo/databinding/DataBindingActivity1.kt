package com.yueban.androidkotlindemo.databinding

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yueban.androidkotlindemo.BR
import com.yueban.androidkotlindemo.R

/**
 * @author yueban
 * @date 2018/12/20
 * @email fbzhh007@gmail.com
 */
class DataBindingActivity1 : AppCompatActivity() {
    private lateinit var mBinding: ActivityDatabinding1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_databinding_1)

        mBinding.message = "set by dataBinding!"
        mBinding.handler = MyHandler()
        mBinding.messageVisible = true
        mBinding.includeTitle = "include title"

        val user = User()
        mBinding.user = user
        user.name.set("ranran")
        user.age.set(5)

        user.liveName.value = "alice"
        user.liveAge.value = 4

        val customObservable = CustomObservable()
        mBinding.customObservable = customObservable
        customObservable.value = "test custom observable"
        mBinding.tvTwoWayDataBinding2.setOnClickListener {
            mBinding.tvTwoWayDataBinding2.time++
        }
    }
}

class MyHandler {
    fun onClick(view: View) {
        Log.d("databinding", "onClicked: $view")
    }

    fun onCallback(viewTag: String) {
        Log.d("databinding", "onCallback: $viewTag")
    }

    fun onLayoutChanged() {}
}

class User {
    val name = ObservableField<String>()
    val age = ObservableInt()
    val liveName = MutableLiveData<String>()
    val liveAge = MutableLiveData<Int>()
    val map = ObservableArrayMap<String, Any>().apply {
        put("message", "emma")
        put("color", ColorStateList.valueOf(Color.RED or Color.BLUE))
    }
}

class CustomObservable : BaseObservable() {
    @get:Bindable
    var value: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.value)
        }

    @get:Bindable
    var num: Int = 1
        set(value) {
            field = value
            Log.d("DataBindingActivity1", "num has been set to: $value")
            notifyPropertyChanged(BR.num)
        }

    @get:Bindable
    var time: Long = System.currentTimeMillis()
        set(value) {
            field = value
            Log.d("DataBindingActivity1", "time has been set to: $value")
            notifyPropertyChanged(BR.time)
        }
}

/**
 * use Observable with ViewModel to get benefit from ViewModel architecture
 *
 * @see [CustomObservable] and [Observable] for more info
 */
class UserModel : ViewModel(), Observable {
    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }

    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }
}