package com.yueban.jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.yueban.jetpack.data.TodoItem
import com.yueban.jetpack.ui.theme.JetpackComposeStateTheme

class MainActivity : ComponentActivity() {
    private val todoVM by viewModels<TodoVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeStateTheme {
                Surface {
                    TodoScreenView(todoVM)
                }
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
private fun TodoScreenView(vm: TodoVM) {
    val items :List<TodoItem> by vm.todoItems.observeAsState(listOf())
    TodoScreen(
        items = items,
        onAddItem = vm::addItem,
        onRemoveItem = vm::removeItem
    )
}