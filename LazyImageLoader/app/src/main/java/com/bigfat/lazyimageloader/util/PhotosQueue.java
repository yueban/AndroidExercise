package com.bigfat.lazyimageloader.util;

import android.widget.ImageView;

import java.util.Stack;

public class PhotosQueue {
    public Stack<PhotoToLoad> photosToloadStack = new Stack<>();

    public void clean(ImageView imageView) {
        for (int i = 0; i < photosToloadStack.size(); i++) {
            if (photosToloadStack.get(i).imageView == imageView) {
                photosToloadStack.remove(i);
            }
        }
    }
}