package com.yueban.androidkotlindemo.demo.paging.db

import android.util.Log
import androidx.lifecycle.LiveData
import com.yueban.androidkotlindemo.demo.paging.model.entity.Repo
import java.util.concurrent.Executor

/**
 * @author yueban
 * @date 2018/12/10
 * @email fbzhh007@gmail.com
 */
class GithubLocalCache(
    private val repoDao: RepoDao,
    private val ioExecutor: Executor
) {
    fun insert(repos: List<Repo>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d(TAG, "inserting ${repos.size} repos")
            repoDao.insert(repos)
            insertFinished()
        }
    }

    fun getReposByName(name: String): LiveData<List<Repo>> {
        val query = "%${name.replace(' ', '%')}%"
        return repoDao.getReposByName(query)
    }

    companion object {
        private const val TAG = "GithubLocalCache"
    }
}