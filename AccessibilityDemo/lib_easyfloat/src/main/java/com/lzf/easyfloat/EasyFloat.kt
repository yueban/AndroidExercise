package com.lzf.easyfloat

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import com.lzf.easyfloat.data.FloatConfig
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.*
import com.lzf.easyfloat.interfaces.OnPermissionResult
import com.lzf.easyfloat.permission.PermissionUtils
import com.lzf.easyfloat.service.FloatService
import com.lzf.easyfloat.utils.LifecycleUtils
import com.lzf.easyfloat.utils.logger
import com.lzf.easyfloat.widget.activityfloat.ActivityFloatManager
import java.lang.ref.WeakReference

/**
 * @author: liuzhenfeng
 * @function: 悬浮窗使用工具类
 * @date: 2019-06-27  15:22
 */
class EasyFloat {

    companion object {
        internal var isDebug: Boolean = false
        // 通过弱引用持有Activity，防止内容泄漏，适用于只有一个浮窗的情况
        private var activityWr: WeakReference<Activity>? = null

        @JvmStatic
        fun init(application: Application, isDebug: Boolean = false) {
            this.isDebug = isDebug
            // 注册Activity生命周期回调
            LifecycleUtils.setLifecycleCallbacks(application)
        }

        @JvmStatic
        fun with(activity: Activity): Builder {
            activityWr = WeakReference(activity)
            return Builder(activity)
        }

        @JvmStatic
        fun with(context: Context): AppBuilder {
            return AppBuilder(context)
        }

        // *************************** Activity浮窗的相关方法 ***************************
        // 通过浮窗管理类，实现相应的功能，详情参考ActivityFloatManager
        @JvmStatic
        fun dismiss(activity: Activity? = null, floatTag: String? = null) =
                manager(activity)?.dismiss(floatTag)

        @JvmStatic
        fun hide(activity: Activity? = null, floatTag: String? = null) =
                manager(activity)?.setVisibility(floatTag, View.GONE)

        @JvmStatic
        fun show(activity: Activity? = null, floatTag: String? = null) =
                manager(activity)?.setVisibility(floatTag, View.VISIBLE)

        @JvmStatic
        fun setDragEnable(
                activity: Activity? = null, dragEnable: Boolean, floatTag: String? = null
        ) = manager(activity)?.setDragEnable(dragEnable, floatTag)

        @JvmStatic
        fun isShow(activity: Activity? = null, floatTag: String? = null) =
                manager(activity)?.isShow(floatTag)

        /**
         * 获取Activity浮窗管理类
         */
        private fun manager(activity: Activity?): ActivityFloatManager? {
            val a: Activity? = activity ?: activityWr?.get()
            return if (a != null) ActivityFloatManager(a) else null
        }

        // *************************** 以下系统浮窗的相关方法 ***************************
        /**
         * 关闭系统级浮窗，发送广播消息，在Service内部接收广播
         */
        @JvmStatic
        fun dismissAppFloat(context: Context, tag: String? = null) =
                FloatService.dismiss(context, tag)

        /**
         * 隐藏系统浮窗，发送广播消息，在Service内部接收广播
         */
        @JvmStatic
        fun hideAppFloat(context: Context, tag: String? = null) =
                FloatService.setVisible(context, false, tag, 0, 0)

        /**
         * 显示系统浮窗，发送广播消息，在Service内部接收广播
         */
        @JvmStatic
        fun showAppFloat(context: Context, tag: String? = null) =
                FloatService.setVisible(context, true, tag)

        @JvmStatic
        fun showAppFloat(context: Context, tag: String? = null, offsetX: Int, offsetY: Int) {
            FloatService.setVisible(context, true, tag, offsetX, offsetY)
        }

        /**
         * 设置系统浮窗是否可拖拽，先获取浮窗的config，后修改相应属性
         */
        @JvmStatic
        fun appFloatDragEnable(dragEnable: Boolean, tag: String? = null) =
                getConfig(tag).let { it?.dragEnable = dragEnable }

        /**
         * 获取系统浮窗是否显示，通过浮窗的config，获取显示状态
         */
        @JvmStatic
        fun appFloatIsShow(tag: String? = null) = getConfig(tag) != null && getConfig(tag)!!.isShow

        /**
         * 以下几个方法为：系统浮窗过滤页面的添加、移除、清空
         */
        @JvmStatic
        fun filterActivity(activity: Activity, tag: String? = null) =
                getConfig(tag).let { it?.filterSet?.add(activity.componentName.className) }

        @JvmStatic
        fun filterActivities(tag: String? = null, vararg clazz: Class<*>) =
                clazz.forEach { c -> getConfig(tag).let { it?.filterSet?.add(c.name) } }

        @JvmStatic
        fun removeFilter(activity: Activity, tag: String? = null) =
                getConfig(tag).let { it?.filterSet?.remove(activity.componentName.className) }

        @JvmStatic
        fun removeFilters(tag: String? = null, vararg clazz: Class<*>) =
                clazz.forEach { c -> getConfig(tag).let { it?.filterSet?.remove(c.name) } }

        @JvmStatic
        fun clearFilters(tag: String? = null) = getConfig(tag)?.filterSet?.clear()

        /**
         * 获取系统浮窗的config
         */
        private fun getConfig(tag: String?) =
                FloatService.floatMap[tag ?: FloatService.DEFAULT_TAG]?.config
    }


    /**
     * 浮窗的属性构建类，支持链式调用
     */
    class Builder(private val activity: Activity) : OnPermissionResult {

        // 创建浮窗数据类，方便管理配置
        private val config = FloatConfig()

        fun setSidePattern(sidePattern: SidePattern): Builder {
            config.sidePattern = sidePattern
            return this
        }

        fun setShowPattern(showPattern: ShowPattern): Builder {
            config.showPattern = showPattern
            return this
        }

        fun setLayout(layoutId: Int): Builder {
            config.layoutId = layoutId
            return this
        }

        fun setGravity(gravity: Int, offsetX: Int = 0, offsetY: Int = 0): Builder {
            config.gravity = gravity
            config.offsetPair = Pair(offsetX, offsetY)
            return this
        }

        fun setLocation(x: Int, y: Int): Builder {
            config.locationPair = Pair(x, y)
            return this
        }

        fun setTag(floatTag: String?): Builder {
            // 如果tag为空，默认使用类名
            config.floatTag = floatTag ?: activity.componentName.className
            return this
        }

        fun setDragEnable(dragEnable: Boolean): Builder {
            config.dragEnable = dragEnable
            return this
        }

        fun invokeView(invokeView: OnInvokeView): Builder {
            config.invokeView = invokeView
            return this
        }

        fun registerCallbacks(callbacks: OnFloatCallbacks): Builder {
            config.callbacks = callbacks
            return this
        }

        fun setAnimator(floatAnimator: OnFloatAnimator?): Builder {
            config.floatAnimator = floatAnimator
            return this
        }

        fun setAppFloatAnimator(appFloatAnimator: OnAppFloatAnimator?): Builder {
            config.appFloatAnimator = appFloatAnimator
            return this
        }

        fun setMatchParent(widthMatch: Boolean = false, heightMatch: Boolean = false): Builder {
            config.widthMatch = widthMatch
            config.heightMatch = heightMatch
            return this
        }

        // 设置需要过滤的Activity，仅对系统浮窗有效
        fun setFilter(vararg clazz: Class<*>): Builder {
            clazz.forEach { config.filterSet.add(it.name) }
            return this
        }

        /**
         * 创建浮窗，包括Activity浮窗和系统浮窗，如若系统浮窗无权限，先进行权限申请
         */
        fun show() {
            if (config.layoutId != null) {
                when {
                    // 仅当页显示，则直接创建activity浮窗
                    config.showPattern == ShowPattern.CURRENT_ACTIVITY -> createActivityFloat()

                    // 系统浮窗需要先进行权限审核，有权限则创建app浮窗
                    PermissionUtils.checkPermission(activity) -> createAppFloat()

                    // 申请浮窗权限
                    else -> PermissionUtils.requestPermission(activity, this)
                }
            } else {
                config.callbacks?.createdResult(false, "未设置浮窗布局文件", null)
                logger.w("未设置浮窗布局文件")
            }
        }

        /**
         * 通过Activity浮窗管理类，创建Activity浮窗
         */
        private fun createActivityFloat() = ActivityFloatManager(activity).createFloat(config)

        /**
         * 通过Service创建系统浮窗
         */
        private fun createAppFloat() = FloatService.startService(activity, config)

        /**
         * 申请浮窗权限的结果回调
         */
        override fun permissionResult(isOpen: Boolean) {
            if (isOpen) createAppFloat()
            else {
                config.callbacks?.createdResult(false, "系统浮窗权限不足，开启失败", null)
                logger.w("系统浮窗权限不足，开启失败")
            }
        }
    }

    /**
     * 浮窗的属性构建类，支持链式调用
     */
    class AppBuilder(private val context: Context) : OnPermissionResult {

        // 创建浮窗数据类，方便管理配置
        private val config = FloatConfig()

        fun setSidePattern(sidePattern: SidePattern): AppBuilder {
            config.sidePattern = sidePattern
            return this
        }

        fun setShowPattern(showPattern: ShowPattern): AppBuilder {
            config.showPattern = showPattern
            return this
        }

        fun setLayout(layoutId: Int): AppBuilder {
            config.layoutId = layoutId
            return this
        }

        fun setGravity(gravity: Int, offsetX: Int = 0, offsetY: Int = 0): AppBuilder {
            config.gravity = gravity
            config.offsetPair = Pair(offsetX, offsetY)
            return this
        }

        fun setLocation(x: Int, y: Int): AppBuilder {
            config.locationPair = Pair(x, y)
            return this
        }

        fun setTag(floatTag: String?): AppBuilder {
            // 如果tag为空，默认使用类名
            config.floatTag = floatTag ?: "AppFloatWindow"
            return this
        }

        fun setDragEnable(dragEnable: Boolean): AppBuilder {
            config.dragEnable = dragEnable
            return this
        }

        fun invokeView(invokeView: OnInvokeView): AppBuilder {
            config.invokeView = invokeView
            return this
        }

        fun registerCallbacks(callbacks: OnFloatCallbacks): AppBuilder {
            config.callbacks = callbacks
            return this
        }

        fun setAnimator(floatAnimator: OnFloatAnimator?): AppBuilder {
            config.floatAnimator = floatAnimator
            return this
        }

        fun setAppFloatAnimator(appFloatAnimator: OnAppFloatAnimator?): AppBuilder {
            config.appFloatAnimator = appFloatAnimator
            return this
        }

        fun setMatchParent(widthMatch: Boolean = false, heightMatch: Boolean = false): AppBuilder {
            config.widthMatch = widthMatch
            config.heightMatch = heightMatch
            return this
        }

        // 设置需要过滤的Activity，仅对系统浮窗有效
        fun setFilter(vararg clazz: Class<*>): AppBuilder {
            clazz.forEach { config.filterSet.add(it.name) }
            return this
        }

        /**
         * 创建浮窗，包括Activity浮窗和系统浮窗，如若系统浮窗无权限，先进行权限申请
         */
        fun show() {
            if (config.layoutId != null) {
                createAppFloat()
            } else {
                config.callbacks?.createdResult(false, "未设置浮窗布局文件", null)
                logger.w("未设置浮窗布局文件")
            }
        }

        /**
         * 通过Service创建系统浮窗
         */
        private fun createAppFloat() = FloatService.startService(context, config)

        /**
         * 申请浮窗权限的结果回调
         */
        override fun permissionResult(isOpen: Boolean) {
            if (isOpen) createAppFloat()
            else {
                config.callbacks?.createdResult(false, "系统浮窗权限不足，开启失败", null)
                logger.w("系统浮窗权限不足，开启失败")
            }
        }
    }

}