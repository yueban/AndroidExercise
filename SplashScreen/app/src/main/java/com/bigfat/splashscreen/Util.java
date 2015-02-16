package com.bigfat.splashscreen;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/16
 */
public class Util {
    public static Bitmap createSingleColorBitmap(int color, int width, int height) {
        int[] colorArray = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                colorArray[y * width + x] = color;
            }
        }
        return Bitmap.createBitmap(colorArray, width, height, Bitmap.Config.ARGB_8888);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
