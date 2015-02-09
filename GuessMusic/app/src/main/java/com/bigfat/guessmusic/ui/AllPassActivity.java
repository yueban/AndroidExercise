package com.bigfat.guessmusic.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;

import com.bigfat.guessmusic.R;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/5
 */
public class AllPassActivity extends ActionBarActivity implements View.OnClickListener {

    public static final String TAG = "AllPassActivity";

    private View mTopBarIconLayout;//顶部加金币按钮模块
    private ImageButton mBtnTopBack;//顶部返回按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_pass);

        initView();
        initViewListener();
    }

    private void initView() {
        mTopBarIconLayout = findViewById(R.id.layout_top_bar_coin);
        mBtnTopBack = (ImageButton) findViewById(R.id.btn_top_bar_back);

        //隐藏添加金币模块
        mTopBarIconLayout.setVisibility(View.INVISIBLE);
    }

    private void initViewListener() {
        mBtnTopBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_bar_back://返回
                finish();
                break;
        }
    }
}
