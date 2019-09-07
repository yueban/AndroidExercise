package com.yueban.androidkotlindemo.databinding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import androidx.databinding.adapters.ViewBindingAdapter
import com.bumptech.glide.Glide
import com.yueban.androidkotlindemo.R

/**
 * @author yueban
 * @date 2018/12/22
 * @email fbzhh007@gmail.com
 */
//@BindingAdapter("android:paddingLeft")
//fun setPaddingLeftPlus200(view: View, padding: Float) {
//    view.setPadding(padding.toInt() + 200, view.paddingTop, view.paddingRight, view.paddingBottom)
//}

//@SuppressLint("SetTextI18n")
//@BindingAdapter("android:text")
//fun setTextWithSuffix(textView: TextView, text: CharSequence?) {
//    textView.text = "$text suffix"
//}

@BindingAdapter("imageUrl", "thumbnail", requireAll = false)
fun loadImage(view: ImageView, url: String?, thumbnail: Drawable?) {
    if (url == null) {
        view.setImageDrawable(thumbnail)
    } else {
        Glide.with(view).load(url).thumbnail(Glide.with(view).load(thumbnail)).into(view)
    }
}

@BindingAdapter(
    "android:onViewDetachedFromWindow",
    "android:onViewAttachedToWindow",
    requireAll = false
)
fun setListener(
    view: View,
    detach: ViewBindingAdapter.OnViewDetachedFromWindow?,
    attach: ViewBindingAdapter.OnViewAttachedToWindow?
) {
    val newListener: View.OnAttachStateChangeListener? =
        if (detach == null && attach == null) {
            null
        } else {
            object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    attach?.onViewAttachedToWindow(v)
                }

                override fun onViewDetachedFromWindow(v: View) {
                    detach?.onViewDetachedFromWindow(v)
                }
            }
        }

    val oldListener: View.OnAttachStateChangeListener? =
        ListenerUtil.trackListener(view, newListener, R.id.onAttachStateChangeListener)
    if (oldListener != null) {
        view.removeOnAttachStateChangeListener(oldListener)
    }
    if (newListener != null) {
        view.addOnAttachStateChangeListener(newListener)
    }
}

/**
 * background attribute with color is work with
 * [androidx.databinding.adapters.Converters]
 *
 * this is method is just a placeholder to make document work correctly
 */
private fun placeHolder() {}

const val ATTR_TIME = "time"

@BindingAdapter(value = [ATTR_TIME])
fun setTime(view: CustomTextView, newValue: Int) {
    if (view.time != newValue) {
        view.time = newValue
    }
}

@InverseBindingAdapter(attribute = ATTR_TIME)
fun getTime(view: CustomTextView): Int {
    return view.time
}

@BindingAdapter("app:timeAttrChanged")
fun setListeners(
    view: CustomTextView,
    attrChange: InverseBindingListener
) {
    view.setOnTimeChangeListener(object : CustomTextView.OnTimeChangeListener {
        override fun onTimeChanged(time: Int) {
            attrChange.onChange()
        }
    })
}