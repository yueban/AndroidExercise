package com.yueban.androidkotlindemo.ui.demo.roomwithview.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.yueban.androidkotlindemo.ui.demo.roomwithview.data.db.WordDao
import com.yueban.androidkotlindemo.ui.demo.roomwithview.data.entity.Word

/**
 * @author yueban
 * @date 2018/12/9
 * @email fbzhh007@gmail.com
 */

class WordRepository(private val wordDao: WordDao) {
    val allWords: LiveData<List<Word>> = wordDao.getAllWords()

    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}