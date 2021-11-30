package com.yueban.jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yueban.jetpack.data.TodoIcon
import com.yueban.jetpack.data.TodoItem
import com.yueban.jetpack.util.generateRandomTodoItem
import kotlin.random.Random

@Composable
fun TodoScreen(
    items: List<TodoItem>,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit) {
    Column {
        LazyColumn(modifier = Modifier.weight(1f),
                   contentPadding = PaddingValues(top = 8.dp)) {
            items(items) {
                TodoRow(todoItem = it,
                        onItemClicked = { onRemoveItem(it) },
                        modifier = Modifier.fillParentMaxWidth()
                )
            }
        }

        Button(
            onClick = { onAddItem(generateRandomTodoItem()) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Add random item")
        }
    }
}

@Composable
fun TodoRow(
    todoItem: TodoItem,
    onItemClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Float = remember(key1 = todoItem.id) { randomTint() }
) {
    Row(
        modifier = modifier
            .clickable { onItemClicked(todoItem) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(todoItem.task)
        Icon(imageVector = todoItem.icon.imageVector,
             tint = LocalContentColor.current.copy(alpha = iconTint),
             contentDescription = stringResource(id = todoItem.icon.contentDescription))
    }
}

private fun randomTint(): Float {
    return Random.nextFloat().coerceIn(0.3f, 0.9f)
}

@Preview
@Composable
fun PreviewTodoScreen() {
    val items = listOf(
        TodoItem("Learn compose", TodoIcon.Event),
        TodoItem("Take the codelab"),
        TodoItem("Apply state", TodoIcon.Done),
        TodoItem("Build dynamic UIs", TodoIcon.Square)
    )
    TodoScreen(items = items, onAddItem = {}, onRemoveItem = {})
}

@Preview
@Composable
fun PreviewTodoRow() {
    val todoItem = remember { generateRandomTodoItem() }
    TodoRow(todoItem = todoItem, onItemClicked = {}, modifier = Modifier.fillMaxWidth())
}