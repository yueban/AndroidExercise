package com.yueban.motionlayoutdemo.sample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class BoundsImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var paint = Paint()

    init {
        paint.setARGB(255, 200, 0, 0)
        paint.strokeWidth = 4f
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawLine(0f, 0f, width.toFloat(), height.toFloat(), paint)
        canvas?.drawLine(0f, height.toFloat(), width.toFloat(), 0f, paint)
    }
}