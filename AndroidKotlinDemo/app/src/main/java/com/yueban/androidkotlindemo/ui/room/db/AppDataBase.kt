package com.yueban.androidkotlindemo.ui.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * @author yueban
 * @date 2018/12/6
 * @email fbzhh007@gmail.com
 */
@Database(
    entities = [User::class],
    views = [UserDetail::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "create table `heiheihei` (`id` integer, `name` text, primary key(`id`))"
        )
    }
}

class DBHolder {
    companion object {
        private var sInstance: AppDataBase? = null

        // 因为 lateinit 对象的 isInitialized 字段有 bug，这里暂时使用 null 判断
        fun get(context: Context): AppDataBase {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "app-database")
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return sInstance!!
        }
    }
}