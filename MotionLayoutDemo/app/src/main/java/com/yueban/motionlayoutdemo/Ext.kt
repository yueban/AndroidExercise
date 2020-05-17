package com.yueban.motionlayoutdemo

import android.app.Activity
import android.content.Intent
import kotlin.reflect.KClass

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020/5/17
 */

fun Activity.startActivity(clazz: KClass<out Activity>) {
    startActivity(Intent(this, clazz.java))
}