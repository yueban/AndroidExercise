package com.lzf.easyfloat.widget.appfloat

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.*
import com.lzf.easyfloat.anim.AppFloatAnimatorManager
import com.lzf.easyfloat.data.FloatConfig
import com.lzf.easyfloat.interfaces.OnFloatTouchListener
import com.lzf.easyfloat.service.FloatService
import com.lzf.easyfloat.utils.DisplayUtils

/**
 * @author: liuzhenfeng
 * @function: 系统浮窗的管理类，包括浮窗的创建、销毁、动画管理等
 * @date: 2019-07-29  16:29
 */
internal class AppFloatManager(val context: Context, var config: FloatConfig) {

    private lateinit var windowManager: WindowManager
    private lateinit var params: WindowManager.LayoutParams
    private var frameLayout: ParentFrameLayout? = null
    private lateinit var touchUtils: TouchUtils


    /**
     * 创建系统浮窗
     */
    fun createFloat() {
        try {
            touchUtils = TouchUtils(config)
            initParams()
            addView()
            config.isShow = true
        } catch (e: Exception) {
            config.callbacks?.createdResult(false, "$e", null)
        }
    }

    private fun initParams() {
        windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        params = WindowManager.LayoutParams().apply {
            // 安卓6.0 以后，全局的Window类别，必须使用TYPE_APPLICATION_OVERLAY
            type =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE
            format = PixelFormat.RGBA_8888
            gravity = Gravity.START or Gravity.TOP
            // 设置浮窗以外的触摸事件可以传递给后面的窗口、不自动获取焦点、可以延伸到屏幕外（设置动画时能用到，动画结束需要去除该属性，不然旋转屏幕可能置于屏幕外部）
            flags =
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            width =
                    if (config.widthMatch) WindowManager.LayoutParams.MATCH_PARENT else WindowManager.LayoutParams.WRAP_CONTENT
            height =
                    if (config.heightMatch) WindowManager.LayoutParams.MATCH_PARENT else WindowManager.LayoutParams.WRAP_CONTENT
            // 如若设置了固定坐标，直接定位
            if (config.locationPair != Pair(0, 0)) {
                x = config.locationPair.first
                y = config.locationPair.second
            }
        }
    }

    /**
     * 将自定义的布局，作为xml布局的父布局，添加到windowManager中，
     * 重写自定义布局的touch事件，实现拖拽效果。
     */
    private fun addView() {
        // 创建一个frameLayout作为浮窗布局的父容器
        frameLayout =
                ParentFrameLayout(context.applicationContext, config)
        // 将浮窗布局文件添加到父容器frameLayout中，并返回该浮窗文件
        val floatingView = LayoutInflater.from(context.applicationContext)
                .inflate(config.layoutId!!, frameLayout, true)
        // 将frameLayout添加到系统windowManager中
        windowManager.addView(frameLayout, params)

        // 通过重写frameLayout的Touch事件，实现拖拽效果
        frameLayout?.touchListener = object : OnFloatTouchListener {
            override fun onTouch(event: MotionEvent) =
                    touchUtils.updateFloat(frameLayout!!, event, windowManager, params)
        }

        // 在浮窗绘制完成的时候，设置初始坐标、执行入场动画
        frameLayout?.layoutListener = object : ParentFrameLayout.OnLayoutListener {
            override fun onLayout() {
                setGravity(frameLayout)
                enterAnim()
            }
        }

        // 设置callbacks
        config.invokeView?.invoke(floatingView)
        config.callbacks?.createdResult(true, null, floatingView)
    }

    /**
     * 设置浮窗的对齐方式，支持上下左右、居中、上中、下中、左中和右中，默认左上角
     * 支持手动设置的偏移量
     */
    @SuppressLint("RtlHardcoded")
    private fun setGravity(view: View?) {
        if (config.locationPair != Pair(0, 0) || view == null) return
        val parentRect = Rect()
        // 获取浮窗所在的矩形
        windowManager.defaultDisplay.getRectSize(parentRect)
        val parentBottom = parentRect.bottom - DisplayUtils.statusBarHeight(view)
        when (config.gravity) {
            // 右上
            Gravity.END, Gravity.END or Gravity.TOP, Gravity.RIGHT, Gravity.RIGHT or Gravity.TOP ->
                params.x = parentRect.right - view.width
            // 左下
            Gravity.START or Gravity.BOTTOM, Gravity.LEFT, Gravity.LEFT or Gravity.BOTTOM ->
                params.y = parentBottom - view.height
            // 右下
            Gravity.END or Gravity.BOTTOM, Gravity.RIGHT or Gravity.BOTTOM -> {
                params.x = parentRect.right - view.width
                params.y = parentBottom - view.height
            }
            // 居中
            Gravity.CENTER -> {
                params.x = ((parentRect.right - view.width) * 0.5f).toInt()
                params.y = ((parentBottom - view.height) * 0.5f).toInt()
            }
            // 上中
            Gravity.CENTER_HORIZONTAL, Gravity.TOP or Gravity.CENTER_HORIZONTAL ->
                params.x = ((parentRect.right - view.width) * 0.5f).toInt()
            // 下中
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL -> {
                params.x = ((parentRect.right - view.width) * 0.5f).toInt()
                params.y = parentBottom - view.height
            }
            // 左中
            Gravity.CENTER_VERTICAL, Gravity.START or Gravity.CENTER_VERTICAL, Gravity.LEFT or Gravity.CENTER_VERTICAL ->
                params.y = ((parentBottom - view.height) * 0.5f).toInt()
            // 右中
            Gravity.END or Gravity.CENTER_VERTICAL, Gravity.RIGHT or Gravity.CENTER_VERTICAL -> {
                params.x = parentRect.right - view.width
                params.y = ((parentBottom - view.height) * 0.5f).toInt()
            }
            // 其他情况，均视为左上
            else -> {
            }
        }

        // 设置偏移量
        params.x += config.offsetPair.first
        params.y += config.offsetPair.second
        // 更新浮窗位置信息
        windowManager.updateViewLayout(view, params)
    }

    @SuppressLint("RtlHardcoded")
    private fun updateGravity(view: View?) {
        if (config.locationPair != Pair(0, 0) || view == null) return
        val parentRect = Rect()
        // 获取浮窗所在的矩形
        windowManager.defaultDisplay.getRectSize(parentRect)
        val parentBottom = parentRect.bottom - DisplayUtils.statusBarHeight(view)
        when (config.gravity) {
            // 右上
            Gravity.END, Gravity.END or Gravity.TOP, Gravity.RIGHT, Gravity.RIGHT or Gravity.TOP ->
                params.x = parentRect.right - view.width
            // 左下
            Gravity.START or Gravity.BOTTOM, Gravity.LEFT, Gravity.LEFT or Gravity.BOTTOM ->
                params.y = parentBottom - view.height
            // 右下
            Gravity.END or Gravity.BOTTOM, Gravity.RIGHT or Gravity.BOTTOM -> {
                params.x = parentRect.right - view.width
                params.y = parentBottom - view.height
            }
            // 居中
            Gravity.CENTER -> {
                params.x = ((parentRect.right - view.width) * 0.5f).toInt()
                params.y = ((parentBottom - view.height) * 0.5f).toInt()
            }
            // 上中
            Gravity.CENTER_HORIZONTAL, Gravity.TOP or Gravity.CENTER_HORIZONTAL ->
                params.x = ((parentRect.right - view.width) * 0.5f).toInt()
            // 下中
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL -> {
                params.x = ((parentRect.right - view.width) * 0.5f).toInt()
                params.y = parentBottom - view.height
            }
            // 左中
            Gravity.CENTER_VERTICAL, Gravity.START or Gravity.CENTER_VERTICAL, Gravity.LEFT or Gravity.CENTER_VERTICAL ->
                params.y = ((parentBottom - view.height) * 0.5f).toInt()
            // 右中
            Gravity.END or Gravity.CENTER_VERTICAL, Gravity.RIGHT or Gravity.CENTER_VERTICAL -> {
                params.x = parentRect.right - view.width
                params.y = ((parentBottom - view.height) * 0.5f).toInt()
            }
            // 其他情况，均视为左上
            else -> {
            }
        }
        // 设置偏移量
        params.x = config.offsetPair.first
        params.y = config.offsetPair.second
        // 更新浮窗位置信息
        windowManager.updateViewLayout(view, params)
    }

    /**
     * 设置浮窗的可见性
     */
    fun setVisible(visible: Int) {
        if (frameLayout == null) return
        frameLayout?.visibility = visible
        if (visible == View.VISIBLE) {
            config.isShow = true
            if (frameLayout!!.childCount > 0) config.callbacks?.show(frameLayout!!.getChildAt(0))
        } else {
            config.isShow = false
            if (frameLayout!!.childCount > 0) config.callbacks?.hide(frameLayout!!.getChildAt(0))
        }
    }

    fun setVisible(visible: Int, offsetX: Int, offsetY: Int) {
        if (frameLayout == null) return
        frameLayout?.visibility = visible
        if (visible == View.VISIBLE) {
            // 更新浮窗位置信息
            config.offsetPair = Pair(offsetX, offsetY)
            updateGravity(frameLayout)
            config.isShow = true
            if (frameLayout!!.childCount > 0) config.callbacks?.show(frameLayout!!.getChildAt(0))
        } else {
            config.isShow = false
            if (frameLayout!!.childCount > 0) config.callbacks?.hide(frameLayout!!.getChildAt(0))
        }
    }

    /**
     * 入场动画
     */
    private fun enterAnim() {
        if (frameLayout == null || config.isAnim) return
        val manager: AppFloatAnimatorManager? =
                AppFloatAnimatorManager(frameLayout!!, params, windowManager, config)
        val animator: Animator? = manager?.enterAnim()
        if (animator == null) {
            // 不需要延伸到屏幕外了，防止屏幕旋转的时候，浮窗处于屏幕外
            params.flags =
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        } else {
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    config.isAnim = false
                    params.flags =
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {
                    config.isAnim = true
                }
            })
            animator.start()
        }
    }

    /**
     * 退出动画
     */
    fun exitAnim() {
        if (frameLayout == null || config.isAnim) return
        val manager: AppFloatAnimatorManager? =
                AppFloatAnimatorManager(frameLayout!!, params, windowManager, config)
        val animator: Animator? = manager?.exitAnim()
        if (animator == null) floatOver() else {
            params.flags =
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) = floatOver()

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {
                    config.isAnim = true
                }
            })
            animator.start()
        }
    }

    /**
     * 退出动画执行结束/没有退出动画，一些回调、移除、检测是否需要关闭Service等操作
     */
    private fun floatOver() {
        config.isAnim = false
        config.callbacks?.dismiss()
        windowManager.removeView(frameLayout)
        FloatService.checkStop(context, config.floatTag)
    }

}