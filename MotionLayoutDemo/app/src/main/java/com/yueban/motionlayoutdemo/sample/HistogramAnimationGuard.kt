package com.yueban.motionlayoutdemo.sample

import androidx.constraintlayout.motion.widget.MotionLayout
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Animation guard helper.
 */
class HistogramAnimationGuard {

    /**
     * @return true if animation should wait. False otherwise.
     */
    val wait: Boolean
    get() {
        if (interruptible) {
            return false
        }
        return animating.get()
    }

    /**
     * Allows animation to be interruptible or not. When interruptible, a new animation can start
     * before the previous animation is finished.
     */
    var interruptible: Boolean = false

    private val animating = AtomicBoolean(false)

    val animationListener: MotionLayout.TransitionListener = object : MotionLayout.TransitionListener {
        override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
            animating.set(true)
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
            animating.set(false)
        }

        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { }
        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) { }
    }

}