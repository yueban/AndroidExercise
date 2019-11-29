package com.yueban.commontest

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.PathUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import java.io.File

class MainActivity : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxPermissions(this).request(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).filter { it }
            .subscribe {
                testFilenameLength()
            }
    }

    private fun testFilenameLength() {
        val path = PathUtils.getExternalAppCachePath()
        File(path).deleteOnExit()
        for (i in 1..1000) {
            try {
                File(path, "\u200B".repeat(i)).createNewFile()
            } catch (e: Exception) {
                e.printStackTrace()
                println("max filename length: ${i - 1}")
                break
            }
        }
    }
}
