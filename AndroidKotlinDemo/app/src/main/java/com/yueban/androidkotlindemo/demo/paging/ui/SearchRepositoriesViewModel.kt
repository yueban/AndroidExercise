package com.yueban.androidkotlindemo.demo.paging.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.yueban.androidkotlindemo.demo.paging.data.GithubRepository
import com.yueban.androidkotlindemo.demo.paging.model.entity.Repo
import com.yueban.androidkotlindemo.demo.paging.model.wrapper.RepoSearchResult

/**
 * @author yueban
 * @date 2018/12/10
 * @email fbzhh007@gmail.com
 */
class SearchRepositoriesViewModel(private val repository: GithubRepository) : ViewModel() {
    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    private val queryLiveData = MutableLiveData<String>()
    private val repoResult: LiveData<RepoSearchResult> = Transformations.map(queryLiveData) {
        repository.search(it)
    }

    val repos: LiveData<PagedList<Repo>> = Transformations.switchMap(repoResult) {
        it.data
    }
    val networkErrors: LiveData<String> = Transformations.switchMap(repoResult) {
        it.networkErrors
    }

    fun searchRepo(query: String) {
        queryLiveData.postValue(query)
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val immutableQuery = lastQueryValue()
            if (immutableQuery != null) {
                repository.requestMore(immutableQuery)
            }
        }
    }

    fun lastQueryValue(): String? = queryLiveData.value
}