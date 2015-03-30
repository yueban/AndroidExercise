package com.bigfat.scrolldemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    int topHeaderHeight;
    private ScrollView scvMain;
    private LinearLayout llTop;
    private TextView tvRemain;
    private ListView lvMain;
    private ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initData();
//        initView();
//        initListView();
//        initEvent();

//        scvMain.post(new Runnable() {
//            //让scrollview跳转到顶部，必须放在runnable()方法中
//            @Override
//            public void run() {
//                scvMain.scrollTo(0, 0);
//            }
//        });
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add("item_" + i);
        }
    }

    private void initView() {
        scvMain = (ScrollView) findViewById(R.id.scv_main);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvRemain = (TextView) findViewById(R.id.tv_remain);
        lvMain = (ListView) findViewById(R.id.lv_main);
    }

    private void initListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_item_tv, data);
        lvMain.setAdapter(adapter);

        int itemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
        int lvHeight = itemHeight * data.size() + lvMain.getDividerHeight() * (data.size() - 1);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lvMain.getLayoutParams();
        lp.height = lvHeight;
        lvMain.setLayoutParams(lp);
    }

    private void initEvent() {
        llTop.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                topHeaderHeight = llTop.getHeight() / 3;
            }
        });

        scvMain.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                Log.i(TAG, "ScrollY--->" + scvMain.getScrollY());
                if (scvMain.getScrollY() < topHeaderHeight * 2) {
                    tvRemain.setVisibility(View.GONE);
                } else {
                    tvRemain.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
