package com.yueban.androidkotlindemo

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.yueban.androidkotlindemo.room.db.AppDataBase
import com.yueban.androidkotlindemo.room.db.User
import com.yueban.androidkotlindemo.room.db.UserDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * @author yueban
 * @date 2018/12/7
 * @email fbzhh007@gmail.com
 */
@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var userDao: UserDao
    private lateinit var db: AppDataBase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val user = User(1, "yueban", "fan", null)
        userDao.insert(user)
        val byName = userDao.findByName("yueban")
        assertEquals(byName, user)
    }
}