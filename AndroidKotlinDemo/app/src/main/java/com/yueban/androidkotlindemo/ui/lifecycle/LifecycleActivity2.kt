package com.yueban.androidkotlindemo.ui.lifecycle

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*

/**
 * implement custom LifecycleRegistry
 *
 * @author yueban
 * @date 2018/12/5
 * @email fbzhh007@gmail.com
 */
class LifecycleActivity2 : AppCompatActivity(), LifecycleOwner {
    private lateinit var mLifecycleRegistry: LifecycleRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLifecycleRegistry = LifecycleRegistry(this)

        lifecycle.addObserver(MyObserver(this))

        mLifecycleRegistry.markState(Lifecycle.State.CREATED)
        // 自定义 mLifecycleRegistry 篡改 Lifecycle 调用逻辑
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED)
//        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)

    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    internal class MyObserver(private val context: Context) : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun printStart() {
            Toast.makeText(context, "start", Toast.LENGTH_SHORT).show()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun printDestroy() {
            Toast.makeText(context, "destroy", Toast.LENGTH_SHORT).show()
        }
    }
}


