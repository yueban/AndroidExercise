package com.yueban.jetpack

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yueban.jetpack.data.TodoIcon
import com.yueban.jetpack.data.TodoItem
import com.yueban.jetpack.util.generateRandomTodoItem

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun TodoScreen(
    items: List<TodoItem>,
    currentEditItem: TodoItem?,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit,
    onEditStart: (TodoItem) -> Unit,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit) {
    Column {
        val enableInputHeader = currentEditItem == null
        TodoItemInputBackground(elevate = enableInputHeader, modifier = Modifier.fillMaxWidth()) {
            if (enableInputHeader) {
                TodoItemEntryInput(onItemComplete = onAddItem)
            } else {
                Text(
                    "Editing item",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
        LazyColumn(modifier = Modifier.weight(1f),
                   contentPadding = PaddingValues(top = 8.dp)) {
            items(items) {
                if (it.id == currentEditItem?.id) {
                    TodoItemInlineEditor(
                        item = currentEditItem,
                        onEditItemChange = onEditItemChange,
                        onEditDone = onEditDone,
                        onRemoveItem = { onRemoveItem(currentEditItem) }
                    )
                } else {
                    TodoRow(
                        todoItem = it,
                        onItemClicked = { onEditStart(it) },
                        modifier = Modifier.fillParentMaxWidth()
                    )
                }
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

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun TodoItemInlineEditor(
    item: TodoItem,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit,
    onRemoveItem: () -> Unit
) {
    TodoItemInput(
        text = item.task,
        onTextChange = { onEditItemChange(item.copy(task = it)) },
        icon = item.icon,
        onIconChange = { onEditItemChange(item.copy(icon = it)) },
        iconVisible = true,
        submit = onEditDone
    ) {
        Row {
            val minWidth = Modifier.widthIn(20.dp)
            val textWidth = Modifier.width(30.dp)
            TextButton(onClick = onEditDone, modifier = minWidth) {
                Text(text = "✅️", textAlign = TextAlign.End, modifier = textWidth)
            }
            TextButton(onClick = onRemoveItem, modifier = minWidth) {
                Text(text = "❌", textAlign = TextAlign.End, modifier = textWidth)
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun TodoItemEntryInput(onItemComplete: (TodoItem) -> Unit) {
    val (text, setText) = remember { mutableStateOf("") }
    val (icon, setIcon) = remember { mutableStateOf(TodoIcon.Default) }
    val iconVisible = text.isNotBlank()
    val submit = {
        onItemComplete(TodoItem(text, icon))
        setIcon(TodoIcon.Default)
        setText("")
    }
    TodoItemInput(
        text = text,
        onTextChange = setText,
        icon = icon,
        onIconChange = setIcon,
        iconVisible = iconVisible,
        submit = submit,
    ) {
        TodoEditButton(onClick = submit, text = "Add", enabled = text.isNotBlank())
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun TodoItemInput(
    text: String,
    onTextChange: (String) -> Unit,
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    iconVisible: Boolean,
    submit: () -> Unit,
    buttonSlot: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
        ) {
            TodoInputText(
                text = text,
                onTextChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                onImeAction = submit
            )

            Box(
                Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp)
            ) { buttonSlot() }
        }

        if (iconVisible) {
            AnimatedIconRow(icon = icon, onIconChange = onIconChange, modifier = Modifier.padding(8.dp))
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TodoRow(
    todoItem: TodoItem,
    onItemClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable { onItemClicked(todoItem) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(todoItem.task)
        Icon(imageVector = todoItem.icon.imageVector,
             tint = LocalContentColor.current.copy(alpha = todoItem.iconTint),
             contentDescription = stringResource(id = todoItem.icon.contentDescription))
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Preview
@Composable
fun PreviewTodoScreen() {
    val items = listOf(
        TodoItem("Learn compose", TodoIcon.Event),
        TodoItem("Take the codelab"),
        TodoItem("Apply state", TodoIcon.Done),
        TodoItem("Build dynamic UIs", TodoIcon.Square)
    )
    TodoScreen(items, null, {}, {}, {}, {}, {})
}

@Preview
@Composable
fun PreviewTodoRow() {
    val todoItem = remember { generateRandomTodoItem() }
    TodoRow(todoItem = todoItem, onItemClicked = {}, modifier = Modifier.fillMaxWidth())
}