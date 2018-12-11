package com.yueban.androidkotlindemo.livedata

import android.util.Log
import androidx.lifecycle.LiveData
import java.math.BigDecimal

/**
 * @author yueban
 * @date 2018/12/5
 * @email fbzhh007@gmail.com
 */
class StockLiveData(symbol: String) : LiveData<BigDecimal>() {
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