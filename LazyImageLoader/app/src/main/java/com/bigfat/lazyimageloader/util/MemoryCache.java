package com.bigfat.lazyimageloader.util;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by yueban on 15/3/26.
 */
public class MemoryCache {
    private HashMap<String, SoftReference<Bitmap>> cache = new HashMap<>();

    public Bitmap get(String id) {
        if (!cache.containsKey(id)) {
            return null;
        }
        SoftReference<Bitmap> ref = cache.get(id);
        return ref.get();
    }

    public void put(String id, Bitmap bitmap) {
        cache.put(id, new SoftReference<Bitmap>(bitmap));
    }

    public void clear() {
        cache.clear();
    }
}
