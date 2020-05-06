package com.yueban.monitorwechatfolder

import android.os.FileObserver
import android.util.Log
import java.io.File

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020/5/6
 */
interface IFileObserver {
    fun init()

    fun printFileObserver(
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
}