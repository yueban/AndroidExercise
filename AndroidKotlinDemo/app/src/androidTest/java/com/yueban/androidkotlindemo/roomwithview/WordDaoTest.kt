package com.yueban.androidkotlindemo.roomwithview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.yueban.androidkotlindemo.roomwithview.util.waitForValue
import com.yueban.androidkotlindemo.ui.demo.roomwithview.data.db.WordDao
import com.yueban.androidkotlindemo.ui.demo.roomwithview.data.db.WordRoomDatabase
import com.yueban.androidkotlindemo.ui.demo.roomwithview.data.entity.Word
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * @author yueban
 * @date 2018/12/9
 * @email fbzhh007@gmail.com
 */
@RunWith(AndroidJUnit4::class)
class WordDaoTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var wordDao: WordDao
    private lateinit var db: WordRoomDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(context, WordRoomDatabase::class.java).allowMainThreadQueries().build()
        wordDao = db.wordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetWord() {
        val word = Word("word")
        wordDao.insert(word)
        val allWords = wordDao.getAllWords().waitForValue()
        assertEquals(allWords[0].word, word.word)
    }


    @Test
    @Throws(Exception::class)
    fun getAllWords() {
        val word = Word("aaa")
        wordDao.insert(word)
        val word2 = Word("bbb")
        wordDao.insert(word2)
        val allWords = wordDao.getAllWords().waitForValue()
        assertEquals(allWords[0].word, word.word)
        assertEquals(allWords[1].word, word2.word)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() {
        val word = Word("word")
        wordDao.insert(word)
        val word2 = Word("word2")
        wordDao.insert(word2)
        wordDao.deleteAll()
        val allWords = wordDao.getAllWords().waitForValue()
        assertTrue(allWords.isEmpty())
    }
}