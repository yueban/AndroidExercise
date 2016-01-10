package com.bigfat.linearlayoutdragdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private LinearLayout llMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llMain = (LinearLayout) findViewById(R.id.ll_main);

        for (int i = 0; i < 50; i++) {
            TextView tv = new TextView(this);
            tv.setText(String.valueOf(i));
            tv.setPadding(10, 10, 10, 10);
            tv.setBackgroundColor(Color.WHITE);
            DragUtil.setupDragSort(tv);
            llMain.addView(tv);
        }
    }

}
