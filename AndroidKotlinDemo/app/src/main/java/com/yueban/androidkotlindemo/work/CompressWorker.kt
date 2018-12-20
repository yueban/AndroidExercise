package com.yueban.androidkotlindemo.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * @author yueban
 * @date 2018/12/11
 * @email fbzhh007@gmail.com
 */
class CompressWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {

        doCompress()

        return Result.success()
//        return Result.failure()
//        return Result.retry()
    }

    private fun doCompress() {
        println("doing compress")
    }
}