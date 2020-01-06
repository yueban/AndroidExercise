package com.csizg.voip.natives;

import android.util.Log;

public class XDNatives {
    public static final String TAG = XDNatives.class.getSimpleName();

    static {
        System.loadLibrary("xindun");
    }

    int encTime = 0;

    int decTime = 0;

    byte[] key = hexString2Bytes("11111111111111111111111111111111");

    private static byte[] hexString2Bytes(String hexString) {
        if (isSpace(hexString)) return null;
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hex2Int(hexBytes[i]) << 4 | hex2Int(hexBytes[i + 1]));
        }
        return ret;
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static int hex2Int(final char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 仅用于测试 java 层能否成功调用 xindun jni 方法
     */
    public native String get();

    public native void init(String info);

    public synchronized byte[] encrypt(byte[] info) {
        int newTime = (int) (System.nanoTime() / 1000 / 1000);
        Log.d(TAG, "encrypt start >>>" + info.length + "\ttime:" + (newTime - encTime));
        encTime = newTime;

//        return EncryptUtils.encryptAES(info, key, "AES/ECB/PKCS5Padding", null);
        return info;
    }

    public synchronized byte[] decrypt(byte[] info) {
        int newTime = (int) (System.nanoTime() / 1000 / 1000);
        Log.d(TAG, "decrypt start >>>" + info.length + "\ttime:" + (newTime - decTime));
        decTime = newTime;

//        return EncryptUtils.decryptAES(info, key, "AES/ECB/PKCS5Padding", null);
        return info;
    }

    @SuppressWarnings("unused")
    public void encryptInit() {
        Log.d(TAG, "encryptInit");
    }

    @SuppressWarnings("unused")
    public void decryptInit() {
        Log.d(TAG, "decryptInit");
    }

    @SuppressWarnings("unused")
    public void destroy() {
        Log.d(TAG, "destroy");
    }

    /**
     * 仅用于测试 xindun jni 层能否成功调用 java 方法
     */
    @SuppressWarnings("unused")
    public void nullMethod() {
    }
}
