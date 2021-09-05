package com.yueban.jetpack.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SlotButtonDemo() {
    Button(
        modifier = Modifier.padding(8.dp),
        onClick = {}
    ) {
        Column {
            Text("some text")
            Divider(
                Modifier
                    .background(color = Color.Black)
                    .height(1.dp)
            )
        }
    }
}