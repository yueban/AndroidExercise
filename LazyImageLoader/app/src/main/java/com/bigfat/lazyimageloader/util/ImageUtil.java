package com.bigfat.lazyimageloader.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yueban on 15/3/26.
 */
public class ImageUtil {

    public static Bitmap url2Bitmap(String urlStr) {
        InputStream inputStream = url2InputStream(urlStr);
        return BitmapFactory.decodeStream(inputStream);
    }

    public static void inputStream2File(InputStream inputStream, File file) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) != -1) {
                bos.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap file2Bitmap(File file) {
        return BitmapFactory.decodeFile(file.getPath());
    }

    public static InputStream url2InputStream(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            return conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
