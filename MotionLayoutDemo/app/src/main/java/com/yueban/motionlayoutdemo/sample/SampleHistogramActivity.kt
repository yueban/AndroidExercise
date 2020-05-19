package com.yueban.motionlayoutdemo.sample

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Switch
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yueban.motionlayoutdemo.R
import kotlinx.android.synthetic.main.activity_sample_histogram.*
import java.util.*
import kotlin.collections.ArrayList

class SampleHistogramActivity : AppCompatActivity() {

    companion object {
        private const val DEFAULT_COLOUR_ID = R.color.colorAccent
        private const val HISTOGRAM_BARS_RESTORE_KEY = "HISTOGRAM"
    }

    // List used for save and restoring data to the widget
    private var bars: ArrayList<HistogramBarMetaData> = ArrayList()

    // The main widget
    private var widget: HistogramWidget? = null

    private val animationGuard: HistogramAnimationGuard = HistogramAnimationGuard()

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_histogram)
        widget = histogram
        restoreView(savedInstanceState)
        widget!!.setTransitionListener(animationGuard.animationListener)
    }

    /**
     * Add random data to the histogram.
     */
    fun onClickAdd(view: View?) {
        if (animationGuard.wait) {
            return
        }
        add()
        widget!!.animateWidget()
    }

    fun onClickSort(view: View?) {
        if (animationGuard.wait) {
            return
        }
        bars = widget!!.sort()
        widget!!.animateWidget()
    }

    fun onClickRandom(view: View) {
        if (animationGuard.wait) {
            return
        }
        add()
        bars = widget!!.sort()
        widget!!.animateWidget()
    }

    private fun add() {
        val rand = Random()
        var barColour = ContextCompat.getColor(this, DEFAULT_COLOUR_ID)
        val barDataList = ArrayList<HistogramBarMetaData>(widget!!.barsSize)
        var name = 0
        for (id in widget!!.barIds) {
            val barData = HistogramBarMetaData(
                    id,
                    rand.nextFloat(),
                    barColour,
                    ColorHelper.getContrastColor(barColour),
                    name.toString())
            barColour = ColorHelper.getNextColor(barColour)
            barDataList.add(barData)
            name++
        }
        bars = barDataList
        widget!!.setData(barDataList)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (bars.isNotEmpty()) {
            outState.putParcelableArrayList(HISTOGRAM_BARS_RESTORE_KEY, bars)
        }
        super.onSaveInstanceState(outState)
    }

    private fun restoreView(savedInstance: Bundle?) {
        if (savedInstance == null) { // nothing to restore.
            return
        }
        bars = savedInstance.getParcelableArrayList(HISTOGRAM_BARS_RESTORE_KEY) ?: ArrayList()
        if (bars.isEmpty()) {
            return
        }

        widget!!.viewTreeObserver.addOnGlobalLayoutListener(object :
                OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Because we're creating all views in code, all the view ids are recreated.
                if (widget!!.barIds.size != bars.size) {
                    throw RuntimeException("Restoring array doesn't match the view size.")
                }

                bars = ArrayList(bars.mapIndexed { i, metaData ->
                    HistogramBarMetaData(widget!!.barIds[i], metaData)
                })
                widget!!.setData(bars)
                widget!!.animateWidget()
                widget!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    fun onClickSwitch(view: View) {
        val animationInterruptible = (view as Switch).isChecked
        animationGuard.interruptible = animationInterruptible
        /**
         * TODO: The current histogram widget does not support interruptible sort to keep it short.
         * This can be a fun exercise to implement yourself.
         *
         * To support this feature, you'll want to animate from:
         * - the current x position to
         * - the new x position (after sorted)
         *
         * for each bars. It means you cannot use chain feature of constraint layout. You'll need
         * to calculate the after-sorted x location of each bars manually and animate them.
         */
        sort.setEnabledAndChangeColor(!animationInterruptible)
        both.setEnabledAndChangeColor(!animationInterruptible)
    }
}

fun View.setEnabledAndChangeColor(enabled: Boolean) {
    if (!enabled) {
        background.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
        isClickable = false
    } else {
        background.colorFilter = null
        isClickable = true
    }
    invalidate()
}
