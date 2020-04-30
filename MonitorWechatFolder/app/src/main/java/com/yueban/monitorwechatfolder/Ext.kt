package com.yueban.monitorwechatfolder

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020/4/28
 */
fun Int.toByteArray(): ByteArray {
    val bytes: ByteArray = ByteArray(4)
    for (i in 0..3) {
        bytes[i] = (this shr (i * 8)).toByte();
        bytes[i] = (this ushr (i * 8)).toByte()
        bytes[i] = ((this shr (i * 8)) and 0xFF).toByte()
    }
    return bytes
}

fun ByteArray.toInt(): Int {
    var sum = 0
    for (i in indices) {
        sum += ((this[i].toInt() and 0xFF) shl i * 8)
    }
    return sum
}
