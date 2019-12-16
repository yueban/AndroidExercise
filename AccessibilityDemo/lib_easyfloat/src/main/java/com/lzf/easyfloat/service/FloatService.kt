package com.lzf.easyfloat.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.view.View
import com.lzf.easyfloat.data.FloatConfig
import com.lzf.easyfloat.utils.logger
import com.lzf.easyfloat.widget.appfloat.AppFloatManager

/**
 * @author: liuzhenfeng
 * @function: 通过Service管理系统浮窗
 * 每次startService，在onStartCommand方法中，通过AppFloatManager创建浮窗，并将manager添加到map集合中，方便管理；
 * 通过接收广播，管理浮窗的销毁和可见性变化；
 * 在销毁浮窗浮窗后，检测map中是否还有别的浮窗存在，如果没有别的浮窗存在，stopService。
 * @date: 2019-07-08  16:02
 */
internal class FloatService : Service() {

    companion object {
        private const val FLOAT_ACTION = "floatAction"
        private const val FLOAT_VISIBLE = "floatVisible"
        private const val FLOAT_DISMISS = "floatDismiss"
        private const val FLOAT_TAG = "floatTag"
        private const val FLOAT_OFFSETX = "floatOffsetX"
        private const val FLOAT_OFFSETY = "floatOffsetY"
        const val DEFAULT_TAG = "default"
        var floatMap = mutableMapOf<String, AppFloatManager>()
        private var config = FloatConfig()

        /**
         * 开启创建浮窗的Service
         */
        fun startService(context: Context, floatConfig: FloatConfig) {
            config = floatConfig
            context.startService(Intent(context, FloatService::class.java))
        }

        /**
         * 关闭浮窗后，检测是否需要关闭Service
         */
        fun checkStop(context: Context, floatTag: String?) {
            // 先清除当条浮窗信息
            if (floatMap.isNotEmpty()) floatMap.remove(floatTag)
            // 如有没有其他浮窗存在，关闭Service
            if (floatMap.isEmpty()) context.stopService(Intent(context, FloatService::class.java))
        }

        /**
         * 设置浮窗可见性，hide or show
         */
        fun setVisible(context: Context, isShow: Boolean, tag: String? = null) =
                context.sendBroadcast(
                        Intent().setAction(FLOAT_ACTION)
                                .putExtra(FLOAT_VISIBLE, isShow)
                                .putExtra(FLOAT_TAG, tag)
                )

        fun setVisible(context: Context, isShow: Boolean, tag: String? = null, offsetX: Int, offsetY: Int) =
                context.sendBroadcast(
                        Intent().setAction(FLOAT_ACTION)
                                .putExtra(FLOAT_VISIBLE, isShow)
                                .putExtra(FLOAT_TAG, tag)
                                .putExtra(FLOAT_OFFSETX, offsetX)
                                .putExtra(FLOAT_OFFSETY, offsetY)
                )

        /**
         * 关闭系统浮窗
         */
        fun dismiss(context: Context, tag: String? = null) =
                context.sendBroadcast(
                        Intent().setAction(FLOAT_ACTION)
                                .putExtra(FLOAT_DISMISS, true)
                                .putExtra(FLOAT_TAG, tag)
                )
    }

    // ***************************** Service 内部逻辑 *****************************
    // 通过广播，接收一些指令（关闭浮窗、设置可见性）
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 过滤掉不满足的条件
            if (intent.action != FLOAT_ACTION || floatMap.isNullOrEmpty()) return
            val tag = intent.getStringExtra(FLOAT_TAG) ?: DEFAULT_TAG
            when {
                // 关闭系统浮窗
                intent.getBooleanExtra(FLOAT_DISMISS, false) -> floatMap[tag]?.exitAnim()

                // 设置浮窗可见
                intent.getBooleanExtra(FLOAT_VISIBLE, true) -> {
                    val offsetX = intent.getIntExtra(FLOAT_OFFSETX, 0)
                    val offsetY = intent.getIntExtra(FLOAT_OFFSETY, 0)
                    if (offsetX != 0 || offsetY != 0) {
                        floatMap[tag]?.setVisible(View.VISIBLE, offsetX, offsetY)
                    } else {
                        floatMap[tag]?.setVisible(View.VISIBLE)
                    }
                }

                // 设置浮窗不可见
                else -> floatMap[tag]?.setVisible(View.GONE)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        // 注册动态广播接收器
        registerReceiver(receiver, IntentFilter().apply { addAction(FLOAT_ACTION) })
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (checkTag()) {
            // 通过floatManager创建浮窗，并将floatManager添加到map中
            floatMap[config.floatTag!!] = AppFloatManager(this, config).apply { createFloat() }
        } else {
            config.callbacks?.createdResult(false, "请为系统浮窗设置不同的tag", null)
            logger.w("请为系统浮窗设置不同的tag")
        }
        return START_NOT_STICKY
    }

    /**
     * 检测浮窗的tag是否有效，不同的浮窗必须设置不同的tag
     */
    private fun checkTag(): Boolean {
        // 如果未设置tag，设置默认tag
        config.floatTag = config.floatTag ?: DEFAULT_TAG
        // map为空使用默认值，有效
        if (floatMap.isEmpty()) return true
        // map不为空，tag比对，存在相同的无效
        floatMap.forEach { (tag, _) -> run { if (tag == config.floatTag) return false } }
        return true
    }

    override fun onDestroy() {
        // 取消广播接收
        unregisterReceiver(receiver)
        super.onDestroy()
    }

}