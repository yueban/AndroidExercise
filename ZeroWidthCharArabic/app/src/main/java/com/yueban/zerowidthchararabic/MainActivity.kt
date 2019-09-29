package com.yueban.zerowidthchararabic

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var mCurrentWord: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_new_word -> {
                nextWord()
            }
            R.id.btn_verify -> {
                verify()
            }
            R.id.btn_copy_to_clipboard -> {
                copyToClipBoard()
            }
            R.id.btn_share -> {
                share()
            }
        }
    }

    private fun nextWord() {
        if (wordsGenerator.hasNext()) {
            mCurrentWord = wordsGenerator.next()
            findViewById<TextView>(R.id.tv_current_word).text = mCurrentWord
        } else {
            Toast.makeText(this, "没有更多单词", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verify() {
        val etVerify: EditText = findViewById(R.id.et_verify)
        val tvVerifyResult: TextView = findViewById(R.id.tv_verify_result)

        val result = StringBuilder()

        // 验证 editText 中字符串
        val editTextEquals = mCurrentWord?.equals(etVerify.text.toString()) ?: false
        result.append("输入框: ")
                .append(if (editTextEquals) "匹配成功" else "匹配失败")
                .append("\n")

        // 验证剪切板中字符串
        val clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.primaryClip?.getItemAt(0)?.text?.toString()?.let {
            val clipboardEquals = mCurrentWord?.equals(it) ?: false
            result.append("剪切板: ")
                    .append(if (clipboardEquals) "匹配成功" else "匹配失败")
        } ?: kotlin.run {
            // 剪切板内容没有内容
            result.append("剪切板没有内容")
        }

        tvVerifyResult.text = result
    }

    private fun copyToClipBoard() {
        mCurrentWord?.let {
            val clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("arabic", mCurrentWord)
            clipboardManager.setPrimaryClip(clipData)
        }
    }

    private fun share() {
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, mCurrentWord)
            startActivity(Intent.createChooser(this, "share using:"))
        }
    }
}
