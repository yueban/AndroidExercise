package com.yueban.jetpack.data

import java.util.UUID
import kotlin.random.Random

data class TodoItem(
    val task: String,
    val icon: TodoIcon = TodoIcon.Default,
    val iconTint: Float = randomTint(),
    val id: UUID = UUID.randomUUID()
) {
    companion object {
        private fun randomTint(): Float {
            return Random.nextFloat().coerceIn(0.3f, 0.9f)
        }
    }
}