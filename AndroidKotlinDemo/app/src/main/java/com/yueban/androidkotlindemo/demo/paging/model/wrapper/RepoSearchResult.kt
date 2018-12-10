package com.yueban.androidkotlindemo.demo.paging.model.wrapper

import androidx.lifecycle.LiveData
import com.yueban.androidkotlindemo.demo.paging.model.entity.Repo

/**
 * @author yueban
 * @date 2018/12/10
 * @email fbzhh007@gmail.com
 */
data class RepoSearchResult(
    val data: LiveData<List<Repo>>,
    val networkErrors: LiveData<String>
)