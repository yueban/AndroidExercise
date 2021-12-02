package com.yueban.jetpack

import com.google.common.truth.Truth.assertThat
import com.yueban.jetpack.util.generateRandomTodoItem
import org.junit.Test

class TodoTest {

    @Test
    fun whenRemovingItem_updatesList() {
        val vm = TodoVM()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        vm.addItem(item1)
        vm.addItem(item2)

        vm.removeItem(item1)

        assertThat(vm.todoItems).isEqualTo(listOf(item2))
    }
}