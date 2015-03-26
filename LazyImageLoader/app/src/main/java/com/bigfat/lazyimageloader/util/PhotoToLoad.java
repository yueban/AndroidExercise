package com.bigfat.lazyimageloader.util;

import android.widget.ImageView;

public class PhotoToLoad {
    public LazyImage lazyImage;
    public ImageView imageView;
    public boolean saveDisk;

    public PhotoToLoad(LazyImage lazyImage, ImageView imageView, boolean saveDisk) {
        this.lazyImage = lazyImage;
        this.imageView = imageView;
        this.saveDisk = saveDisk;
    }
}