package com.yueban.sunflower.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.yueban.sunflower.data.AppDatabase
import com.yueban.sunflower.data.Plant
import com.yueban.sunflower.utilities.PLANT_DATA_FILENAME

/**
 * @author yueban
 * @date 2018/12/17
 * @email fbzhh007@gmail.com
 */
class SeedDatabaseWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    companion object {
        private val TAG by lazy { SeedDatabaseWorker::class.java.simpleName }
    }

    override fun doWork(): Result {
        val plantType = object : TypeToken<List<Plant>>() {}.type
        var jsonReader: JsonReader? = null

        return try {
            val inputStream = applicationContext.assets.open(PLANT_DATA_FILENAME)
            jsonReader = JsonReader(inputStream.reader())
            val plantList: List<Plant> = Gson().fromJson(jsonReader, plantType)
            val database = AppDatabase.getInstance(applicationContext)
            database.plantDao().insertAll(plantList)
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error seeding database", e)
            Result.failure()
        } finally {
            jsonReader?.close()
        }
    }
}