package com.bigfat.reboundtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

public class MainActivity extends AppCompatActivity {
    // Create a system to run the physics loop for a set of springs.
    private SpringSystem springSystem;
    private Spring spring;
    //控件
    private ImageView imageView;
    private SeekBar sbFriction;
    private TextView tvFriction;
    private SeekBar sbTension;
    private TextView tvTension;
    //数值
    private int tension = 40;
    private int friction = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind view
        imageView = (ImageView) findViewById(R.id.image_view);
        sbTension = (SeekBar) findViewById(R.id.sb_tension);
        tvTension = (TextView) findViewById(R.id.tv_tension);
        sbFriction = (SeekBar) findViewById(R.id.sb_friction);
        tvFriction = (TextView) findViewById(R.id.tv_friction);

        //set listener
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (seekBar.getId()) {
                    case R.id.sb_tension:
                        tension = progress;
                        tvTension.setText("tension:" + progress);
                        break;

                    case R.id.sb_friction:
                        friction = progress;
                        tvFriction.setText("friction:" + progress);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SpringConfig config = new SpringConfig(tension, friction);
                spring.setSpringConfig(config);
            }
        };
        sbTension.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbFriction.setOnSeekBarChangeListener(onSeekBarChangeListener);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        spring.setEndValue(1);
                        return true;

                    case MotionEvent.ACTION_UP:
                        spring.setEndValue(0);
                        return true;
                }
                return false;
            }
        });

        //init spring
        //创建系统用于循环执行控件弹簧效果
        springSystem = SpringSystem.create();
        //给系统添加一个“弹簧”
        spring = springSystem.createSpring();
        //添加监听器，监听“弹簧”的形变
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                imageView.setScaleX(scale);
                imageView.setScaleY(scale);
            }
        });
        //根据张力系数和阻力系数创建一组“弹簧”参数
        SpringConfig config = new SpringConfig(tension, friction);
        //配置“弹簧”
        spring.setSpringConfig(config);

        //init view
        sbTension.setMax(100);
        sbFriction.setMax(30);
        sbTension.setProgress(tension);
        sbFriction.setProgress(friction);
    }

    public void onClick(View view) {
        Uri uri = Uri.parse("webcal://meihua.mingdao.com/Apps/calendar/exportCal_DBF78ABAE2931B5BA6B551154B6B4C1.ics");
        Intent calendarIntent = new Intent(Intent.ACTION_SEND, uri);
        calendarIntent.setType("text/calendar");
        final Intent chooser = Intent.createChooser(calendarIntent, "choose");
        startActivity(chooser);
    }
}
