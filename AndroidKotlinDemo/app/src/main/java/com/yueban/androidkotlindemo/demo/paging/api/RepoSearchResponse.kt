package com.yueban.androidkotlindemo.demo.paging.api

import com.google.gson.annotations.SerializedName
import com.yueban.androidkotlindemo.demo.paging.model.entity.Repo

/**
 * @author yueban
 * @date 2018/12/10
 * @email fbzhh007@gmail.com
 */
data class RepoSearchResponse(
    @SerializedName("total") val total: Int = 0,
    @SerializedName("items") val items: List<Repo> = emptyList(),
    val nextPage: Int? = null
)