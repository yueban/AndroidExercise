package com.yueban.jetpack

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData

val clickData = MutableLiveData(0)

@Composable
fun TestClickCounter() {
    val clicks by clickData.observeAsState()
    ClickCounter(clicks!!) {
        clickData.value = clickData.value!! + 1
    }
}

@Composable
fun ClickCounter(clicks: Int, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("I've been clicked $clicks times")
    }
}