package com.yueban.monitorwechatfolder

import android.os.Environment
import android.os.FileObserver
import android.util.Log
import java.io.File

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020/5/6
 */
object WeChatFileObserver : IFileObserver {
    private lateinit var downloadFileObserver: FileObserver
    private lateinit var imgFileObserver: FileObserver

    override fun init() {
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
}