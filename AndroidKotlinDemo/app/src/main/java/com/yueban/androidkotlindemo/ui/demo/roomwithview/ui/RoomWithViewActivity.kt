package com.yueban.androidkotlindemo.ui.demo.roomwithview.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.yueban.androidkotlindemo.R
import com.yueban.androidkotlindemo.ui.demo.roomwithview.data.entity.Word
import com.yueban.androidkotlindemo.ui.demo.roomwithview.ui.adapter.WordListAdapter
import com.yueban.androidkotlindemo.ui.demo.roomwithview.ui.viewmodel.WordViewModel
import kotlinx.android.synthetic.main.activity_room_with_view.*
import kotlinx.android.synthetic.main.content_main.*

/**
 * @author yueban
 * @date 2018/12/9
 * @email fbzhh007@gmail.com
 */
class RoomWithViewActivity : AppCompatActivity() {
    private lateinit var wordViewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_with_view)

        val adapter = WordListAdapter(this)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)

        wordViewModel.allWords.observe(this, Observer { words ->
            words.let { adapter.setWords(it) }
        })

        fab.setOnClickListener {
            val intent = Intent(this@RoomWithViewActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.let {
                val word = Word(it.getStringExtra(NewWordActivity.EXTRA_REPLY))
                wordViewModel.insert(word)
            }
        } else {
            Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val newWordActivityRequestCode = 1
    }
}