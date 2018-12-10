package com.yueban.androidkotlindemo.demo.roomwithview.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yueban.androidkotlindemo.R
import kotlinx.android.synthetic.main.activity_new_word.*

/**
 * @author yueban
 * @date 2018/12/9
 * @email fbzhh007@gmail.com
 */
class NewWordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)

        button_save.setOnClickListener {
            if (edit_word.text.isNullOrEmpty()) {
                setResult(Activity.RESULT_CANCELED)
            } else {
                val word = edit_word.text.toString()
                val replyIntent = Intent()
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}