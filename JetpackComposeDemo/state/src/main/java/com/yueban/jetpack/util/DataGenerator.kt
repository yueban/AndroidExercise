package com.yueban.jetpack.util

import com.yueban.jetpack.data.TodoIcon
import com.yueban.jetpack.data.TodoItem

fun generateRandomTodoItem(): TodoItem {
    val message = listOf(
        "Learn compose",
        "Learn state",
        "Build dynamic UIs",
        "Learn Unidirectional Data Flow",
        "Integrate LiveData",
        "Integrate ViewModel",
        "Remember to savedState!",
        "Build stateless composables",
        "Use state from stateless composables"
    ).random()
    val icon = TodoIcon.values().random()
    return TodoItem(message, icon)
}