package com.yueban.jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import com.yueban.jetpack.ui.theme.JetpackComposeStateTheme

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
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
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
private fun TodoScreenView(vm: TodoVM) {
    TodoScreen(
        items = vm.todoItems,
        onAddItem = vm::addItem,
        onRemoveItem = vm::removeItem
    )
}