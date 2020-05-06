package com.yueban.monitorwechatfolder

import android.content.Context
import android.media.ExifInterface
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020/5/6
 */
object FileEditor {
    private const val EXIF_INFO = "xindun_110"
    private const val PDF_SUFFIX_FLAG: Byte = 1

    fun readPDF(): String {
        val imgFile =
            File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "_yueban" + File.separator + "weekly.pdf")

        val raf = RandomAccessFile(imgFile, "r")

        // 读取芯盾特征值
        raf.seek(raf.length() - 10)
        val flagBuffer = ByteArray(10)
        raf.read(flagBuffer)
        var isXindunEncPdf = true
        for (b in flagBuffer) {
            if (b != PDF_SUFFIX_FLAG) {
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


            return "check isXindunEncPdf: $isXindunEncPdf\n" +
                    "data length: $length\n" +
                    "data: ${String(dataBytes)}"
        } else {
            return "check isXindunEncPdf: $isXindunEncPdf\n"
        }
    }

    fun writePDF(context: Context) {
        val input = context.assets.open("weekly.pdf")

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
        val encryptedData = ("xindun_".repeat(10000)).toByteArray()
        out.write(encryptedData)
        out.write(encryptedData.size.toByteArray())
        // 写入 xindun 特征值
        out.write(ByteArray(10) { PDF_SUFFIX_FLAG })
        out.flush()
        out.close()
    }

    fun readImage(): String {
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

        return "check UserComment: $isUserComment\n" +
                "check DeviceSettingDescription: $isDeviceSettingDescription\n" +
                "data length: $length\n" +
                "data: ${String(dataBytes)}"
    }

    fun writeDataToJpeg(context: Context) {
        val input = context.assets.open("test.jpeg")

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
        val encryptedData = ("xindun_".repeat(10000)).toByteArray()
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
}