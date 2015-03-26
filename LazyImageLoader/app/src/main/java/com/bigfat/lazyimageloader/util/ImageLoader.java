package com.bigfat.lazyimageloader.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by yueban on 15/3/26.
 */
public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private MemoryCache memoryCache;
    private FileCache fileCache;
    private Map<ImageView, String> imageViews;

    private PhotosLoaderThread photosLoaderThread;
    private PhotosQueue photosQueue;
    private int defaultImageResId;

    public ImageLoader(Context context, int defaultImageResId) {
        photosLoaderThread = new PhotosLoaderThread();
        photosLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
        memoryCache = new MemoryCache();
        fileCache = new FileCache(context);
        imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
        photosQueue = new PhotosQueue();
        this.defaultImageResId = defaultImageResId;
    }

    public void displayImage(LazyImage lazyImage, ImageView imageView) {
        displaImage(lazyImage, imageView, true);
    }

    public void displaImage(LazyImage lazyImage, ImageView imageView, boolean saveDisk) {
        imageViews.put(imageView, lazyImage.toFileName());
        if (lazyImage.getBitmap() != null) {
            imageView.setImageBitmap(lazyImage.getBitmap());
        } else {
            Bitmap bitmap = memoryCache.get(lazyImage.toFileName());
            if (bitmap != null) {
                lazyImage.setBitmap(bitmap);
                imageView.setImageBitmap(bitmap);
            } else {
                if (defaultImageResId > 0) {
                    imageView.setImageResource(defaultImageResId);
                } else {
                    imageView.setImageBitmap(null);
                }
                if (lazyImage.getImage_url() != null) {
                    queuePhoto(lazyImage, imageView, saveDisk);
                }
            }
        }
    }

    public void stopThread() {
        photosLoaderThread.interrupt();
    }

    private void queuePhoto(LazyImage lazyImage, ImageView imageView, boolean saveDisk) {
        photosQueue.clean(imageView);
        PhotoToLoad photoToLoad = new PhotoToLoad(lazyImage, imageView, saveDisk);
        synchronized (photosQueue.photosToloadStack) {
            photosQueue.photosToloadStack.push(photoToLoad);
            photosQueue.photosToloadStack.notifyAll();
        }
        if (photosLoaderThread.getState() == Thread.State.NEW) {
            photosLoaderThread.start();
        }
    }


    private Bitmap getBitmap(LazyImage lazyImage, boolean saveDisk) {
        if (!saveDisk) {
            return ImageUtil.url2Bitmap(lazyImage.getImage_url());
        }
        File file = fileCache.getFile(lazyImage);
        Bitmap bitmap = ImageUtil.file2Bitmap(file);
        if (bitmap != null) {
            lazyImage.setBitmap(bitmap);
            return bitmap;
        }

        try {
            ImageUtil.inputStream2File(ImageUtil.url2InputStream(lazyImage.getImage_url()), file);
            lazyImage.setBitmap(ImageUtil.file2Bitmap(file));
            return lazyImage.getBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class PhotosLoaderThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    if (photosQueue.photosToloadStack.size() == 0) {
                        synchronized (photosQueue.photosToloadStack) {
                            photosQueue.photosToloadStack.wait();
                        }
                    }
                    if (photosQueue.photosToloadStack.size() != 0) {
                        PhotoToLoad photoToLoad;
                        synchronized (photosQueue.photosToloadStack) {
                            photoToLoad = photosQueue.photosToloadStack.pop();
                        }
                        Bitmap bitmap = getBitmap(photoToLoad.lazyImage, photoToLoad.saveDisk);
                        memoryCache.put(photoToLoad.lazyImage.toFileName(), bitmap);
                        String tag = imageViews.get(photoToLoad.imageView);
                        if (tag != null && tag.equals(photoToLoad.lazyImage.toFileName())) {
                            BitmapDisplayer bitmapDisplayer = new BitmapDisplayer(bitmap, photoToLoad.imageView);
                            Activity activity = (Activity) photoToLoad.imageView.getContext();
                            activity.runOnUiThread(bitmapDisplayer);
                        }
                    }
                    if (Thread.interrupted()) {
                        break;
                    }
                }
            } catch (InterruptedException e) {

            }
        }
    }

    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        ImageView imageView;

        public BitmapDisplayer(Bitmap bitmap, ImageView imageView) {
            this.bitmap = bitmap;
            this.imageView = imageView;
        }

        @Override
        public void run() {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(defaultImageResId);
            }
        }
    }
}