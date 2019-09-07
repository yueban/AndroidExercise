package com.yueban.autosizetext_with_showmore

import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view_1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter1 = ItemAdapter()
        recycler_view_1.adapter = adapter1
        adapter1.setData((1..6).map { Constant.STR_1 })

        recycler_view_2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter2 = ItemAdapter()
        recycler_view_2.adapter = adapter2
        adapter2.setData((1..6).map { Constant.STR_2 })

        recycler_view_3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter3 = ItemAdapter()
        recycler_view_3.adapter = adapter3
        adapter3.setData((1..6).map { Constant.STR_3 })
    }

    class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemVH>() {
        private val mData: MutableList<String> = mutableListOf()

        fun setData(data: List<String>) {
            mData.clear()
            mData.addAll(data)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
            val layoutResId = when (viewType) {
                0 -> R.layout.item_1
                1 -> R.layout.item_2
                2 -> R.layout.item_3
                3 -> R.layout.item_4
                4 -> R.layout.item_5
                5 -> R.layout.item_6
                else -> 0
            }
            val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
            return ItemVH(view)
        }

        override fun getItemCount(): Int = mData.size

        override fun onBindViewHolder(holder: ItemVH, position: Int) {
            holder.bind(mData[position])
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val mTextView: TextView = itemView.findViewById(R.id.text_view)
            private var mContentOnGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

            fun bind(text: String) {
                fixMaxLine()
                mTextView.text = text
            }



            private fun fixMaxLine() {
                mContentOnGlobalLayoutListener =
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            val layout = mTextView.layout
                            if (layout != null) {
                                mTextView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                                // 判断是否有字符
                                // layout.getXXX(lines) 索引都是从 0 开始
                                val lines = mTextView.lineCount
                                if (lines > 0) {
                                    val text = mTextView.text

                                    if (mTextView.maxLines > 0 && mTextView.maxLines < Integer.MAX_VALUE) {// 指定 maxLine
                                        // 通过 ellipsisCount 判断字符长度是否超过可见范围
                                        val ellipsisCount = layout.getEllipsisCount(lines - 1)

                                        if (ellipsisCount > 0) {
                                            setFixedContent(layout, text, lines)
                                        }
                                    } else {// 未指定 maxLines
                                        // 通过计算 visibleLines 判断字符长度是否超过可见范围
                                        val height = mTextView.height
                                        val scrollY = mTextView.scrollY
                                        val bottomY = scrollY + height
                                        val firstVisibleLineNumber = layout.getLineForVertical(scrollY)
                                        val lastVisibleLineNumber = layout.getLineForVertical(bottomY)
                                        var visibleLines = lastVisibleLineNumber + 1 - firstVisibleLineNumber
                                        // 修正最后一行不是完全可见的情况，如果 visibleLines 实际高度与 bottomY 差值小于 5，则认为最后一行完全可见
                                        if (mTextView.lineHeight * visibleLines - bottomY > 5) {
                                            visibleLines -= 1
                                        }
                                        val visibleTextLength = layout.getLineEnd(visibleLines - 1)
                                        val textLength = text.length

                                        if (visibleTextLength < textLength) {
                                            setFixedContent(layout, text, visibleLines)
                                        }
                                    }
                                }
                            }
                        }
                    }
                mTextView.viewTreeObserver.addOnGlobalLayoutListener(mContentOnGlobalLayoutListener)
            }

            private fun setFixedContent(
                layout: Layout,
                originText: CharSequence,
                visibleLines: Int
            ) {
                val ellipsisText = "..."

                val showMoreText = SpannableString("show more")
                showMoreText.setSpan(
                    ForegroundColorSpan(Color.BLUE),
                    0,
                    showMoreText.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                val textSb = SpannableStringBuilder()
                //裁剪原文本，多裁剪一行，并在末尾一行拼接『全文』label
                textSb.append(
                    originText.subSequence(
                        0, (layout.getLineStart(visibleLines - 1) - 1 // 上一行末尾字符 index
                            - ellipsisText.length)  // 表示有字符被缩略的占位字符长度
                            + 1
                    )
                )
                    .append(ellipsisText)
                    .append('\n')
                    .append(showMoreText)
                mTextView.text = textSb
            }
        }
    }
}