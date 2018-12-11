package com.yueban.androidkotlindemo.work

import android.content.Context
import androidx.work.*

/**
 * @author yueban
 * @date 2018/12/11
 * @email fbzhh007@gmail.com
 */
const val KEY_X_ARG = "X"
const val KEY_Y_ARG = "Y"

const val KEY_RESULT = "result"

class MathWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val x = inputData.getInt(KEY_X_ARG, 0)
        val y = inputData.getInt(KEY_Y_ARG, 0)
        val result = doMathOperation(x, y)


        val output: Data = workDataOf(KEY_RESULT to result)
        return Result.success(output)
    }

    private fun doMathOperation(x: Int, y: Int): Int {
        Thread.sleep((x + y).toLong() * 1000)
        return x * y
    }
}
