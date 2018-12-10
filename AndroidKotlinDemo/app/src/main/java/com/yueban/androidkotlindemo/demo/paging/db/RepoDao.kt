package com.yueban.androidkotlindemo.demo.paging.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yueban.androidkotlindemo.demo.paging.model.entity.Repo

/**
 * @author yueban
 * @date 2018/12/10
 * @email fbzhh007@gmail.com
 */
@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repos: List<Repo>)

    @Query("select * from repos where (name like :query) or (description like :query) order by stars desc, name asc")
    fun getReposByName(query: String): LiveData<List<Repo>>
}