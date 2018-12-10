package com.yueban.androidkotlindemo.demo.paging

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.yueban.androidkotlindemo.demo.paging.api.GithubService
import com.yueban.androidkotlindemo.demo.paging.data.GithubRepository
import com.yueban.androidkotlindemo.demo.paging.db.GithubLocalCache
import com.yueban.androidkotlindemo.demo.paging.db.RepoDatabase
import com.yueban.androidkotlindemo.demo.paging.ui.ViewModelFactory
import java.util.concurrent.Executors

/**
 * @author yueban
 * @date 2018/12/10
 * @email fbzhh007@gmail.com
 */
object Injection {
    private fun provideCache(context: Context): GithubLocalCache {
        val database = RepoDatabase.getInstance(context)
        return GithubLocalCache(database.repoDao(), Executors.newSingleThreadExecutor())
    }

    private fun provideGithubRepository(context: Context): GithubRepository {
        return GithubRepository(GithubService.create(), provideCache(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideGithubRepository(context))
    }
}