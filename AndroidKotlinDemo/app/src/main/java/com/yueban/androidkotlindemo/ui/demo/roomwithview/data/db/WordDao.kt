package com.yueban.androidkotlindemo.ui.demo.roomwithview.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yueban.androidkotlindemo.ui.demo.roomwithview.data.entity.Word

/**
 * @author yueban
 * @date 2018/12/9
 * @email fbzhh007@gmail.com
 */
@Dao
interface WordDao {
    @Query("select * from word order by word asc")
    fun getAllWords(): LiveData<List<Word>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(word: Word)

    @Query("delete from word")
    fun deleteAll()
}