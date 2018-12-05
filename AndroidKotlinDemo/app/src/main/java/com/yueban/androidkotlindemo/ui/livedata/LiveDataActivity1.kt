package com.yueban.androidkotlindemo.ui.livedata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProviders
import com.yueban.androidkotlindemo.R
import kotlinx.android.synthetic.main.activity_live_data_1.*
import java.math.BigDecimal

/**
 * @author yueban
 * @date 2018/12/5
 * @email fbzhh007@gmail.com
 */
class LiveDataActivity1 : AppCompatActivity() {
    private lateinit var mModel1: LiveDataViewModel1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data_1)

        /********** demo 1 **********/
        mModel1 = ViewModelProviders.of(this).get(LiveDataViewModel1::class.java)
        val messageObserver = Observer<String> { newMessage ->
            message.text = newMessage
        }
        mModel1.message.observe(this, messageObserver)
        message_button.setOnClickListener {
            if (mModel1.message.value.isNullOrEmpty()) {
                mModel1.message.value = "new message"
            } else {
                mModel1.message.value += "1"
            }
        }


        /********** demo 2.1 **********/
        val myPriceListener: LiveData<BigDecimal> = StockLiveData("milk")
        myPriceListener.observe(this, Observer<BigDecimal> { price: BigDecimal? ->
            // Update the UI.
        })

        /********** demo 2.2 **********/
        // known bug: https://youtrack.jetbrains.com/issue/KT-21862
//        val myPriceListener2: LiveData<BigDecimal> = StockLiveDataSingleton.get("milk")
//        myPriceListener.observe(this, Observer<BigDecimal> { price: BigDecimal? ->
//            // Update the UI.
//        })


        /********** demo 3.1 **********/
        val liveData: LiveData<BigDecimal> = StockLiveData("milk")
        val liveDataStr1: LiveData<String> = Transformations.map(liveData) {
            it.toString()
        }
        val liveDataStr2 = Transformations.switchMap(liveData) {
            val a = object : LiveData<String>() {
                public override fun setValue(value: String?) {
                    super.setValue(value)
                }
            }
            a.value = it.toString()
            a
        }

        /********** demo 3.2 **********/
        val model2 = LiveDataViewModel2(PostalCodeRepository())
        model2.postalCode.observe(this, Observer<String> {
            postcode.text = it
        })

        postcode_button.setOnClickListener {
            model2.setInput(System.currentTimeMillis().toString())
        }
    }
}