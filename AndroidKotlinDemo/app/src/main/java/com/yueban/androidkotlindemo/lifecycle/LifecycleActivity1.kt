package com.yueban.androidkotlindemo.lifecycle

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.Snackbar

/**
 * implement custom LifecycleObserver
 *
 * @author yueban
 * @date 2018/12/5
 * @email fbzhh007@gmail.com
 */
class LifecycleActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myObserver = MyObserver(this)
        lifecycle.addObserver(myObserver)

        val myLocationListener = MyLocationListener(this, lifecycle) {
            Log.d("location", it.toString())
            Snackbar.make(window.decorView, it.toString(), Snackbar.LENGTH_SHORT).show()
        }
        lifecycle.addObserver(myLocationListener)

        // check if userStatus is legal to enable the listener
        val userStatus = true
        if (userStatus) {
            myLocationListener.enable()
        }
    }

    internal class MyObserver(private val context: Context) : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun connectListener() {
            Toast.makeText(context, "on start: connect listener", Toast.LENGTH_SHORT).show()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun disconnectListener() {
            Toast.makeText(context, "on pause: disconnect listener", Toast.LENGTH_SHORT).show()
        }
    }

    internal class MyLocationListener(
        private val context: Context,
        private val lifecycle: Lifecycle,
        private val callback: (Location) -> Unit
    ) : LifecycleObserver {
        private var enabled = false
        private var location: Location? = null

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun start() {
            if (enabled) {
                // connect
                location = Location("this is a location string at onStart lifecycle")
                invokeCallback()
            }
        }

        fun enable() {
            enabled = true
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                // connect if not connected
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun stop() {
            // disconnect
            location = Location("this is a location string at onStop lifecycle")
            invokeCallback()
        }

        private fun invokeCallback() {
            location?.let(callback)
        }
    }
}