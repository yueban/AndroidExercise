package com.yueban.jetpack

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.yueban.jetpack.data.TodoItem

class TodoVM : ViewModel() {
    private var currentEditPosition by mutableStateOf(-1)
    val currentEditItem: TodoItem?
        get() = todoItems.getOrNull(currentEditPosition)

    var todoItems = mutableStateListOf<TodoItem>()
        private set

    fun addItem(item: TodoItem) {
        todoItems.add(item)
    }

    fun removeItem(item: TodoItem) {
        todoItems.remove(item)
        onEditDone()
    }

    fun onEditItemSelected(todoItem: TodoItem) {
        currentEditPosition = todoItems.indexOf(todoItem)
    }

    fun onEditDone() {
        currentEditPosition = -1
    }

    fun onEditItemChange(item: TodoItem) {
        val currentItem = requireNotNull(currentEditItem)
        require(currentItem.id == item.id) {
            "You can only change an item with the same id as currentEditItem"
        }
        todoItems[currentEditPosition] = item
    }
}