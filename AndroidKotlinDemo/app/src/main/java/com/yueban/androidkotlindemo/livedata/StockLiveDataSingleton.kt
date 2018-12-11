package com.yueban.androidkotlindemo.livedata

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import java.math.BigDecimal

/**
 * @author yueban
 * @date 2018/12/5
 * @email fbzhh007@gmail.com
 */
class StockLiveDataSingleton(symbol: String) : LiveData<BigDecimal>() {
    companion object {
        private lateinit var sInstance: StockLiveDataSingleton

        @MainThread
        fun get(symbol: String): StockLiveDataSingleton {
            // known bug: https://youtrack.jetbrains.com/issue/KT-21862
            sInstance = if (::sInstance.isInitialized) sInstance else StockLiveDataSingleton(symbol)
            return sInstance
        }
    }

    private val mStockManager = StockManager(symbol)

    private val mListener = { price: BigDecimal ->
        value = price
    }

    override fun onActive() {
        mStockManager.requestPriceUpdates(mListener)
    }

    override fun onInactive() {
        mStockManager.removeUpdates(mListener)
    }

    inner class StockManager(private val symbol: String) {
        fun requestPriceUpdates(listener: (BigDecimal) -> Unit) {
            Log.d("StockManager", "get price updates by symbol name: $symbol")
        }

        fun removeUpdates(listener: (BigDecimal) -> Unit) {
            Log.d("StockManager", "remove updates by symbol name: $symbol")
        }
    }
}