package com.yueban.androidkotlindemo.demo.roomwithview.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yueban.androidkotlindemo.R
import com.yueban.androidkotlindemo.demo.roomwithview.data.entity.Word

/**
 * @author yueban
 * @date 2018/12/9
 * @email fbzhh007@gmail.com
 */
class WordListAdapter internal constructor(context: Context) : RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var words = emptyList<Word>()

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.item_word, parent, false)
        return WordViewHolder(itemView)
    }

    override fun getItemCount(): Int = words.size

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = words[position]
        holder.wordItemView.text = current.word
    }

    internal fun setWords(words: List<Word>) {
        this.words = words
        notifyDataSetChanged()
    }
}