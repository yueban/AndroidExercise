package com.yueban.jetpack

import androidx.compose.runtime.Composable
import com.yueban.jetpack.data.TodoItem

@Composable
fun TodoScreen(
    items: List<TodoItem>,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit) {

}