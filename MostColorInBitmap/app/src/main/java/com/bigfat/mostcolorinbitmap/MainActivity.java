package com.bigfat.mostcolorinbitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private LinearLayout llMain;
    private ImageView img01;
    private ImageView img02;
    private MyHandler mHandler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    private void initView() {
        img01 = (ImageView) findViewById(R.id.img_main_01);
        img02 = (ImageView) findViewById(R.id.img_main_02);
        llMain = (LinearLayout) findViewById(R.id.ll_main_bg);
    }

    private void initEvent() {
        img01.setOnClickListener(this);
        img02.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_main_01:
                changeBgColorByImg(BitmapFactory.decodeResource(getResources(), R.mipmap.img_01));
                break;

            case R.id.img_main_02:
                changeBgColorByImg(BitmapFactory.decodeResource(getResources(), R.mipmap.img_02));
                break;
        }
    }

    private void changeBgColorByImg(Bitmap bitmap) {
        new Thread(new GetMostColorFromBitmapRunnable(bitmap)).start();
    }

    private int getMostColorFromBitmap(Bitmap bitmap) {
        Log.i(TAG, "width--->" + bitmap.getWidth());
        Log.i(TAG, "height--->" + bitmap.getHeight());

        int flag = 10;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int widthPeriod = width / flag + 1;
        int heightPeriod = height / flag + 1;

        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        int mostColorCount = 0;

        for (int i = 0; i < flag; i++) {
            int widthIndex = widthPeriod * i;
            if (widthIndex > width - 1) {
                break;
            }
            for (int j = 0; j < flag; j++) {
                int heightIndex = heightPeriod * i;
                if (heightIndex > height - 1) {
                    break;
                }
                int pixel = bitmap.getPixel(widthIndex, heightIndex);
                if (map.containsKey(pixel)) {
                    int colorCount = map.get(pixel) + 1;
                    mostColorCount = colorCount > mostColorCount ? colorCount : mostColorCount;
                    map.put(pixel, colorCount);
                } else {
                    map.put(pixel, 1);
                }
            }
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (mostColorCount == entry.getValue()) {
                return entry.getKey();
            }
        }
        return 0;
    }

    private class MyHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("MainActivity", "what--->" + msg.what);
            if (msg.what != 0) {
                llMain.setBackgroundColor(msg.what);
            }
        }
    }

    private class GetMostColorFromBitmapRunnable implements Runnable {
        private Bitmap bitmap;

        public GetMostColorFromBitmapRunnable(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            if (bitmap != null) {
                int color = getMostColorFromBitmap(bitmap);
                bitmap.recycle();
                mHandler.sendEmptyMessage(color);
            }
        }
    }
}
