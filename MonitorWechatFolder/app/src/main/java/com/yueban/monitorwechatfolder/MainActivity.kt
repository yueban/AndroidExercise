package com.yueban.monitorwechatfolder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        btn_write_img.setOnClickListener {
            FileEditor.writeDataToJpeg(this)
        }
        btn_read_img.setOnClickListener {
            tv_img_info.text = FileEditor.readImage()
        }
        btn_write_pdf.setOnClickListener {
            FileEditor.writePDF(this)
        }
        btn_read_pdf.setOnClickListener {
            tv_img_info.text = FileEditor.readPDF()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                finish()
            }
        }
        WeChatFileObserver.init()
    }
}