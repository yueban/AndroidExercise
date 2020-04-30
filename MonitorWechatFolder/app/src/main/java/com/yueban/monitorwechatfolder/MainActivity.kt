package com.yueban.monitorwechatfolder

import android.Manifest
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.os.Bundle
import android.os.Environment
import android.os.FileObserver
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile


class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var downloadFileObserver: FileObserver
    private lateinit var imgFileObserver: FileObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        btn_write_img.setOnClickListener {
            writeDataToJpeg()
        }
        btn_read_img.setOnClickListener {
            readImage()
        }
        btn_write_pdf.setOnClickListener {
            writePDF()
        }
        btn_read_pdf.setOnClickListener {
            readPDF()
        }
    }

    private fun readPDF() {
        val imgFile =
            File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "_yueban" + File.separator + "weekly.pdf")

        val raf = RandomAccessFile(imgFile, "r")

        // 读取芯盾特征值
        raf.seek(raf.length() - 10)
        val flagBuffer = ByteArray(10)
        raf.read(flagBuffer)
        var isXindunEncPdf = true
        val flagByte: Byte = 0
        for (b in flagBuffer) {
            if (b != flagByte) {
                isXindunEncPdf = false
            }
        }

        if (isXindunEncPdf) {
            // read length
            raf.seek(raf.length() - 14)
            val lengthBuffer = ByteArray(4)
            raf.read(lengthBuffer)
            val length = lengthBuffer.toInt()

            // read data
            val dataBytes = ByteArray(length)
            raf.seek(raf.length() - 14 - length)
            raf.read(dataBytes)
            raf.close()


            tv_img_info.text =
                "check isXindunEncPdf: $isXindunEncPdf\n" +
                        "data length: $length\n" +
                        "data: ${String(dataBytes)}"
        } else {
            tv_img_info.text =
                "check isXindunEncPdf: $isXindunEncPdf\n"
        }
    }

    private fun writePDF() {
        val input = resources.assets.open("weekly.pdf")

        val outFile =
            File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "_yueban" + File.separator + "weekly.pdf")
        if (outFile.exists()) {
            outFile.delete()
        }
        outFile.parentFile?.mkdirs()
        outFile.createNewFile()

        val out = FileOutputStream(outFile)

        val buffer = ByteArray(1024)
        var len: Int = input.read(buffer)
        while (len != -1) {
            out.write(buffer, 0, len)
            len = input.read(buffer)
        }
        input.close()

        // 写入隐藏信息
        val encryptedData = ("xindun_".repeat(10)).toByteArray()
        out.write(encryptedData)
        out.write(encryptedData.size.toByteArray())
        // 写入 xindun 特征值
        out.write(byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        out.flush()
        out.close()
    }

    private fun readImage() {
        val imgFile =
            File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "_yueban" + File.separator + "test.jpeg")

        val raf = RandomAccessFile(imgFile, "r")

        // read length
        raf.seek(raf.length() - 4)
        val lengthBuffer = ByteArray(4)
        raf.read(lengthBuffer)
        val length = lengthBuffer.toInt()

        // read data
        val dataBytes = ByteArray(length)
        raf.seek(raf.length() - 4 - length)
        raf.read(dataBytes)
        raf.close()

        // read metadata
        val imgExif = ExifInterface(imgFile)
        val isUserComment = (imgExif.getAttribute("UserComment") ?: "") == EXIF_INFO
        val isDeviceSettingDescription =
            (imgExif.getAttribute("DeviceSettingDescription") ?: "") == EXIF_INFO

        tv_img_info.text =
            "check UserComment: $isUserComment\n" +
                    "check DeviceSettingDescription: $isDeviceSettingDescription\n" +
                    "data length: $length\n" +
                    "data: ${String(dataBytes)}"
    }

    private fun writeDataToJpeg() {
        val input = resources.assets.open("test.jpeg")

        val outFile =
            File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "_yueban" + File.separator + "test.jpeg")
        if (outFile.exists()) {
            outFile.delete()
        }
        outFile.parentFile?.mkdirs()
        outFile.createNewFile()

        val out = FileOutputStream(outFile)

        val buffer = ByteArray(1024)
        var len: Int = input.read(buffer)
        while (len != -1) {
            out.write(buffer, 0, len)
            len = input.read(buffer)
        }
        input.close()

        // 写入隐藏信息
        val encryptedData = ("xindun_".repeat(10)).toByteArray()
        out.write(encryptedData)
        out.write(encryptedData.size.toByteArray())
        out.flush()
        out.close()

        // 写入 metadata
        val exif = ExifInterface(outFile)

        exif.setAttribute("UserComment", EXIF_INFO)
        exif.setAttribute("DeviceSettingDescription", EXIF_INFO)

        exif.saveAttributes()
    }

    private fun init() {
        val rootDirectory = Environment.getExternalStorageDirectory()
        val wxDownloadFolder =
            File(rootDirectory.absolutePath + File.separator + "tencent" + File.separator + "MicroMsg" + File.separator + "Download")
        Log.d(TAG, "wxDownloadFolder: " + wxDownloadFolder.absolutePath)
//        Log.d("file", Arrays.toString(wxDownloadFolder.list()))

        downloadFileObserver = object : FileObserver(wxDownloadFolder) {
            override fun onEvent(event: Int, path: String?) {
                if (path == null || path == "test_writable") {
                    return
                }

                if (event == MOVED_TO) {
                    printFileObserver(event, path, wxDownloadFolder, "img")
                }
            }
        }
        downloadFileObserver.startWatching()


        val wxImgFolder =
            File(rootDirectory.absolutePath + File.separator + "tencent" + File.separator + "MicroMsg" + File.separator + "Weixin")
        Log.d(TAG, "wxImgFolder: " + wxImgFolder.absolutePath)
        imgFileObserver = object : FileObserver(wxImgFolder) {
            override fun onEvent(event: Int, path: String?) {
                if (path == null || path == "test_writable") {
                    return
                }

                if (event == CLOSE_WRITE) {
                    printFileObserver(event, path, wxImgFolder, "download")
                }
            }
        }
        imgFileObserver.startWatching()
    }

    private fun printFileObserver(
        event: Int,
        path: String?,
        parentFolder: File,
        fileType: String
    ) {
        val eventStr = when (event) {
            FileObserver.ACCESS -> "ACCESS"
            FileObserver.MODIFY -> "MODIFY"
            FileObserver.ATTRIB -> "ATTRIB"
            FileObserver.CLOSE_WRITE -> "CLOSE_WRITE"
            FileObserver.CLOSE_NOWRITE -> "CLOSE_NOWRITE"
            FileObserver.OPEN -> "OPEN"
            FileObserver.MOVED_FROM -> "MOVED_FROM"
            FileObserver.MOVED_TO -> "MOVED_TO"
            FileObserver.CREATE -> "CREATE"
            FileObserver.DELETE -> "DELETE"
            FileObserver.DELETE_SELF -> "DELETE_SELF"
            FileObserver.MOVE_SELF -> "MOVE_SELF"
            else -> event.toString()
        }

        Log.d(
            TAG,
            "new wx $fileType file event: event: $eventStr, $path, filesize: ${File(
                parentFolder,
                path!!
            ).length()}"
        )
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
        init()
    }

    companion object {
        const val TAG = "yueban-wx"
        const val EXIF_INFO = "xindun_110"
    }
}