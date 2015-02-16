package com.bigfat.game_pintu.util;

import android.graphics.Bitmap;

/**
 * 拼图碎片
 *
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/16
 */
public class ImagePiece {
    private int index;
    private Bitmap bitmap;

    public ImagePiece(int index, Bitmap bitmap) {
        this.index = index;
        this.bitmap = bitmap;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "ImagePiece{" +
                "index=" + index +
                ", bitmap=" + bitmap +
                '}';
    }
}