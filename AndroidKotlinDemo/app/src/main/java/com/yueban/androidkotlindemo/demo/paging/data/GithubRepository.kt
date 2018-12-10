package com.yueban.androidkotlindemo.demo.paging.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.yueban.androidkotlindemo.demo.paging.api.GithubService
import com.yueban.androidkotlindemo.demo.paging.api.searchRepos
import com.yueban.androidkotlindemo.demo.paging.db.GithubLocalCache
import com.yueban.androidkotlindemo.demo.paging.model.wrapper.RepoSearchResult

/**
 * @author yueban
 * @date 2018/12/10
 * @email fbzhh007@gmail.com
 */


class GithubRepository(
    private val service: GithubService,
    private val cache: GithubLocalCache
) {
    private var lastRequestedPage = 1
    private val networkErrors = MutableLiveData<String>()
    private var isRequestInProgress = false

    fun search(query: String): RepoSearchResult {
        Log.d(TAG, "New query: $query")
        lastRequestedPage = 1
        requestAndSaveData(query)

        val data = cache.getReposByName(query)

        return RepoSearchResult(data, networkErrors)
    }

    fun requestMore(query: String) {
        requestAndSaveData(query)
    }

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
            cache.insert(repos) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }

    companion object {
        private const val TAG = "GithubRepository"
        private const val NETWORK_PAGE_SIZE = 50
    }
}