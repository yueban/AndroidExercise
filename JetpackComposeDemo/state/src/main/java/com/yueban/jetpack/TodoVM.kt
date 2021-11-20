package com.yueban.jetpack

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yueban.jetpack.data.TodoItem

class TodoVM : ViewModel() {
    private var _todoItems = MutableLiveData(listOf<TodoItem>())
    val todoItems: LiveData<List<TodoItem>> = _todoItems

    fun addItem(item: TodoItem) {
    }

    fun removeItem(item: TodoItem) {
    }
}