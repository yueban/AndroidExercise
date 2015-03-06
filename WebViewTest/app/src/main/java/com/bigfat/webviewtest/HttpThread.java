package com.bigfat.webviewtest;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/6
 */
public class HttpThread extends Thread {

    private static final String TAG = "HttpThread";

    private String mUrl;

    public HttpThread(String url) {
        this.mUrl = url;
    }

    @Override
    public void run() {
        Log.i(TAG,"download--->start");
        InputStream in = null;
        FileOutputStream out = null;
        try {
            URL httpUrl = new URL(mUrl);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");

            in = conn.getInputStream();
            File downloadFile;
            File sdFile;
            //检查SD卡是否挂载
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                downloadFile = Environment.getExternalStorageDirectory();
                sdFile = new File(downloadFile, "test.apk");
                out = new FileOutputStream(sdFile);
            }

            byte[] b = new byte[6 * 1024];
            int length;
            while ((length = in.read(b)) != -1) {
                if (out != null) {
                    out.write(b, 0, length);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG,"download--->end");
    }
}
