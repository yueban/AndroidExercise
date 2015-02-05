package com.bigfat.guessmusic.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.bigfat.guessmusic.R;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/5
 */
public class AllPassActivity extends ActionBarActivity {

    public static final String TAG = "AllPassActivity";

    private View mTopBarIconLayout;//顶部加金币按钮模块

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_pass);

        initView();
    }

    private void initView() {
        mTopBarIconLayout = findViewById(R.id.layout_top_bar_coin);

        //隐藏添加金币模块
        mTopBarIconLayout.setVisibility(View.INVISIBLE);
    }
}
