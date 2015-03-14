package com.bigfat.androidltest;

import android.animation.Animator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.util.Pair;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.Toolbar;

import com.bigfat.androidltest.model.Paper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

//    private DragFrameLayout dflMain;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ImageButton imgBtnFloatButton;
    public static List<Paper> paperList;
    //表示是否是添加状态
    private boolean isAdd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode().setDuration(1000));
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initToolbar();
        initEvent();
        initFloatButton();
        initRecyclerView();
    }

    private void initView() {
//        dflMain = (DragFrameLayout) findViewById(R.id.dfl_main);
        toolbar = (Toolbar) findViewById(R.id.tb_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        imgBtnFloatButton = (ImageButton) findViewById(R.id.imgbtn_main_float_button);
    }

    private void initToolbar() {
        setActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            getActionBar().setTitle("壁纸推荐");
        }
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SampleDivider(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, paperList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initFloatButton() {
        //浮动按钮裁剪
        imgBtnFloatButton.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int shapeSize = (int) getResources().getDimension(R.dimen.shape_size);
                outline.setRoundRect(0, 0, shapeSize, shapeSize, shapeSize / 2);
            }
        });
        imgBtnFloatButton.setClipToOutline(true);
        //浮动按钮拖拽阴影
//        dflMain.setOnDragFrameLayoutListener(new OnDragFrameLayoutListener() {
//
//            @Override
//            public void onDragDrop(View view, boolean captured) {
//                view.animate().translationZ(captured ? 30 : 0).setDuration(100);
//            }
//        });
//        dflMain.addDragView(imgBtnFloatButton);
//        dflMain.addDragView(findViewById(R.id.circle));
    }

    private void initData() {
        if (paperList == null) {
            paperList = new ArrayList<>();
        }
        paperList.clear();
        addData(0);
    }

    private void addData(int position) {
        paperList.add(position, new Paper(C.NAMES[paperList.size()], C.PICS[paperList.size()], C.WORKS[paperList.size()], C.PIC_GROUPS[paperList.size()]));
    }

    private void removeData(int position) {
        paperList.remove(position);
    }

    private void initEvent() {
        imgBtnFloatButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_main_float_button:
                Animator animator = createCircularRevealAnimator(v);
                animator.start();

                int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (position == RecyclerView.NO_POSITION) {
                    position = 0;
                }
                if (recyclerViewAdapter.getItemCount() != C.NAMES.length && isAdd) {
                    addData(position);
                    recyclerViewAdapter.notifyItemInserted(position);
                } else {
                    removeData(position);
                    recyclerViewAdapter.notifyItemRemoved(position);
                }
//                recyclerViewAdapter.notifyDataSetChanged();
//                recyclerView.scrollToPosition(recyclerViewAdapter.getItemCount() - 1);

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
        View pic = v.findViewById(R.id.img_list_item_pic);
        //退出Activity时的组件动画
        getWindow().setExitTransition(new ChangeBounds().setDuration(300));
        //共享组件
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, Pair.create((View) toolbar, toolbar.getTransitionName()), Pair.create(pic, position + "pic"), Pair.create((View) imgBtnFloatButton, imgBtnFloatButton.getTransitionName())).toBundle();

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("position", position);
        startActivity(intent, bundle);
    }

    /**
     * 获取圆形切小放大动画
     *
     * @param v 要执行动画的View
     */
    private Animator createCircularRevealAnimator(View v) {
        Animator animator = ViewAnimationUtils.createCircularReveal(v, v.getWidth() / 2, v.getHeight() / 2, 0, v.getWidth());
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);
        return animator;
    }
}
