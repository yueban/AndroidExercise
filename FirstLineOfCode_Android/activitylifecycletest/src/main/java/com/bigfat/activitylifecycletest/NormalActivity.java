package com.bigfat.activitylifecycletest;

import android.app.Activity;
import android.os.Bundle;


public class NormalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_layout);
    }
}
