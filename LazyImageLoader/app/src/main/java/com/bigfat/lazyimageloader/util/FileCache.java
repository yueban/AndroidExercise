package com.bigfat.lazyimageloader.util;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by yueban on 15/3/26.
 */
public class FileCache {
    private File cacheDir;

    public FileCache(Context context) {
        cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"yueban/cache");
        Log.d(this.getClass().getSimpleName(), "cacheDir:" + cacheDir);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    public File getFile(LazyImage lazyImage) {
        String filename = lazyImage.toFileName();
        return new File(cacheDir, filename);
    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        for (File file : files) {
            file.delete();
        }
    }
}
