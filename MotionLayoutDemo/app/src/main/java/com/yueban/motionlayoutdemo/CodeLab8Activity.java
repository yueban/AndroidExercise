package com.yueban.motionlayoutdemo;

import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

public class CodeLab8Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_lab8);

        coordinateMotion();
    }

    private void coordinateMotion() {
        final AppBarLayout appBarLayout = findViewById(R.id.appbar_layout);
        final MotionLayout motionLayout = findViewById(R.id.motion_layout);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout layout, int i) {
                float seekPosition = -i * 1.0f / appBarLayout.getTotalScrollRange();
                motionLayout.setProgress(seekPosition);
            }
        });
    }
}
