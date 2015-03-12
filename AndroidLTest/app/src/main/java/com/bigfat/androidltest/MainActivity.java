package com.bigfat.androidltest;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;

import com.bigfat.androidltest.model.Paper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    public static DisplayMetrics displayMetrics;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ImageButton imgBtnFloatButton;
    private List<Paper> paperList;
    //表示是否是添加状态
    private boolean isAdd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayMetrics = getResources().getDisplayMetrics();

        initActionBar();
        initView();
        initFloatButton();
        initData();
        initEvent();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, paperList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("壁纸分享");
        }
    }


    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.main_rv);
        imgBtnFloatButton = (ImageButton) findViewById(R.id.imgbtn_float_button);
    }

    private void initFloatButton() {
        imgBtnFloatButton.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int shapeSize = (int) getResources().getDimension(R.dimen.shape_size);
                outline.setRoundRect(0, 0, shapeSize, shapeSize, shapeSize / 2);
            }
        });
        imgBtnFloatButton.setClipToOutline(true);
    }

    private void initData() {
        if (paperList == null) {
            paperList = new ArrayList<>();
        }
        paperList.clear();
        addData();
    }

    private void addData() {
        paperList.add(new Paper(C.NAMES[paperList.size()], C.PICS[paperList.size()], C.WORKS[paperList.size()], C.PIC_GROUPS[paperList.size()]));
    }

    private void removeData() {
        paperList.remove(paperList.size() - 1);
    }

    private void initEvent() {
        imgBtnFloatButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_float_button:
                if (recyclerViewAdapter.getItemCount() != C.NAMES.length && isAdd) {
                    addData();
                } else {
                    removeData();
                }
                recyclerViewAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(recyclerViewAdapter.getItemCount() - 1);

                //调整增减状态
                if (recyclerViewAdapter.getItemCount() == 0) {
                    imgBtnFloatButton.setImageDrawable(getDrawable(android.R.drawable.ic_input_add));
                    isAdd = true;
                }
                if (recyclerViewAdapter.getItemCount() == C.NAMES.length) {
                    imgBtnFloatButton.setImageDrawable(getDrawable(android.R.drawable.ic_delete));
                    isAdd = false;
                }
                break;
        }
    }

    public void startActivity(final View v, final int position) {
        Log.i(TAG, "startActivity___position--->" + position);
    }
}
