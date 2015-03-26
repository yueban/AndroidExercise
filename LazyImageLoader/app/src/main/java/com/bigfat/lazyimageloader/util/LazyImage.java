package com.bigfat.lazyimageloader.util;

import android.graphics.Bitmap;

/**
 * Created by yueban on 15/3/26.
 */
public class LazyImage {
    private Bitmap bitmap;
    private String image_url;
    private long timestamp;

    public LazyImage(String image_url) {
        this(image_url, 0);
    }

    public LazyImage(String image_url, long timestamp) {
        this(null, image_url, timestamp);
    }

    public LazyImage(Bitmap bitmap, String image_url, long timestamp) {
        this.bitmap = bitmap;
        this.image_url = image_url;
        this.timestamp = timestamp;
    }

    public boolean hasLoadPhoto() {
        return bitmap != null;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        this.hashCode();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String toFileName() {
        String fileName = "";
        if (image_url != null && image_url.contains("/")) {
            fileName = image_url.substring(image_url.lastIndexOf("/") + 1);
        }
        return fileName + "_" + timestamp;
    }
}
