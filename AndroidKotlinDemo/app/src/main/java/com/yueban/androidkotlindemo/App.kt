package com.yueban.androidkotlindemo

import android.app.Application
import com.yueban.androidkotlindemo.work.WorkDemo

/**
 * @author yueban
 * @date 2018/12/11
 * @email fbzhh007@gmail.com
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        WorkDemo.testWork()
    }
}