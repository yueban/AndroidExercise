package com.yueban.androidkotlindemo.ui.demo.roomwithview.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.yueban.androidkotlindemo.ui.demo.roomwithview.data.WordRepository
import com.yueban.androidkotlindemo.ui.demo.roomwithview.data.db.WordRoomDatabase
import com.yueban.androidkotlindemo.ui.demo.roomwithview.data.entity.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @author yueban
 * @date 2018/12/9
 * @email fbzhh007@gmail.com
 */
class WordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WordRepository
    val allWords: LiveData<List<Word>>

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init {
        val wordDao = WordRoomDatabase.getDatabase(application, scope).wordDao()
        repository = WordRepository(wordDao)
        allWords = repository.allWords
    }

    fun insert(word: Word) = scope.launch(Dispatchers.IO) {
        repository.insert(word)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}