package com.yueban.androidkotlindemo.demo.paging.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yueban.androidkotlindemo.demo.paging.data.GithubRepository

/**
 * @author yueban
 * @date 2018/12/10
 * @email fbzhh007@gmail.com
 */
class ViewModelFactory(private val repository: GithubRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchRepositoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchRepositoriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}