package com.yueban.androidkotlindemo.work

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Result
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * @author yueban
 * @date 2018/12/11
 * @email fbzhh007@gmail.com
 */
class WorkDemo {
    companion object {
        fun testWork() {
            /********** demo 1 **********/
//            val compressionWork = OneTimeWorkRequestBuilder<CompressWorker>().build()
//            enqueueCompressionWork(compressionWork)

            /********** demo 2.1 **********/
//            val myConstraints = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                Constraints.Builder()
//                    .setRequiresDeviceIdle(true)
//                    .setRequiresCharging(true)
//                    .build()
//            } else {
//                Constraints.Builder()
//                    .setRequiresCharging(true)
//                    .build()
//            }
//            val compressionWork = OneTimeWorkRequestBuilder<CompressWorker>()
//                .setConstraints(myConstraints)
//                /********** part of demo 2.3 **********/
//                .addTag("compression")
//                .build()
//            enqueueCompressionWork(compressionWork)
//            /********** demo 2.2 **********/
////            WorkManager.getInstance().cancelWorkById(compressionWork.id)
//            /********** demo 2.3 **********/
//            WorkManager.getInstance().cancelAllWorkByTag("compression")

            /********** demo 3 **********/
//            val compressionWork = PeriodicWorkRequestBuilder<CompressWorker>(12, TimeUnit.HOURS).build()
//            enqueueCompressionWork(compressionWork)

            /********** demo 4 **********/
//            val workA = OneTimeWorkRequestBuilder<CompressWorker>().build()
//            val workB = OneTimeWorkRequestBuilder<CompressWorker>().build()
//            val workC = OneTimeWorkRequestBuilder<CompressWorker>().build()
//            val workD = OneTimeWorkRequestBuilder<CompressWorker>().build()
//            val workE = OneTimeWorkRequestBuilder<CompressWorker>().build()
//
//            /********** demo 4.1 **********/
//            WorkManager.getInstance()
//                .beginWith(workA)
//                .then(workB)
//                .then(Arrays.asList(workC, workD))
//                .enqueue()
//
//            /********** demo 4.2 **********/
//            val chain1 = WorkManager.getInstance()
//                .beginWith(workA)
//                .then(workB)
//            val chain2 = WorkManager.getInstance()
//                .beginWith(workC)
//                .then(workD)
//            val chain3 = WorkContinuation
//                .combine(Arrays.asList(chain1, chain2))
//                .then(workE)
//            chain3.enqueue()
//
//            /********** demo 4.3 **********/
//            WorkManager.getInstance()
//                .beginUniqueWork("work_unique_name", ExistingWorkPolicy.REPLACE, workA)
//                .then(workB)
//                .enqueue()
//            WorkManager.getInstance()
//                .beginUniqueWork("work_unique_name", ExistingWorkPolicy.APPEND, workC)
//                .then(workD)
//                .then(workE)
//                .enqueue()

            /********** demo 5 **********/
//            val data: Data = workDataOf(
//                KEY_X_ARG to 1,
//                KEY_Y_ARG to 2
//            )
//            val mathWork = OneTimeWorkRequestBuilder<MathWorker>()
//                .setInputData(data)
//                .build()
//
//            /********** demo 5.1 **********/
////        WorkManager.getInstance().enqueue(mathWork)
////        WorkManager.getInstance().getWorkInfoByIdLiveData(mathWork.id)
////            .observe(ProcessLifecycleOwner.get(), Observer { workInfo ->
////                workInfo?.let {
////                    if (workInfo.state.isFinished) {
////                        val result = workInfo.outputData.getInt(KEY_RESULT, 0)
////                        println("mathWork result: $result")
////                    }
////                }
////            })
//
//            /********** demo 5.2 **********/
//            val nextWork = OneTimeWorkRequestBuilder<NextWorker>().build()
//            WorkManager.getInstance().beginWith(mathWork).then(nextWork).enqueue()
        }

        /********** demo 5.2 **********/
        class NextWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
            override fun doWork(): Result {
                val result = inputData.getInt(KEY_RESULT, 0)
                println("result in NextWorker is: $result")
                return Result.success()
            }
        }

        /********** demo 1,2,3 **********/
        private fun enqueueCompressionWork(compressionWork: WorkRequest) {
            WorkManager.getInstance().enqueue(compressionWork)

            WorkManager.getInstance().getWorkInfoByIdLiveData(compressionWork.id).observe(
                ProcessLifecycleOwner.get(),
                Observer { workInfo ->
                    workInfo?.let {
                        if (workInfo.state.isFinished) {
                            println("compressionWork finished")
                        }
                    }
                })
        }
    }
}