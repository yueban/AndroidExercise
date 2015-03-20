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
        //开启转场动画功能
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //设置转场动画
        getWindow().setEnterTransition(new ChangeBounds().setDuration(1000));
        getWindow().setExitTransition(new Explode().setDuration(300));

        setContentView(R.layout.activity_main);

        initView();
        initData();
        initToolbar();
        initEvent();
        initFloatButton();
        initRecyclerView();
    }

    private void initView() {
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
        //设置Item动画，这里使用系统实现的DefaultItemAnimator类
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化Adapter
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
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //初始化/清空数据列表
        if (paperList == null) {
            paperList = new ArrayList<>();
        }
        paperList.clear();
        //添加一个数据
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
            case R.id.imgbtn_main_float_button://点击后添加/删除一个元素
                //浮动按钮点击动画
                Animator animator = createCircularRevealAnimator(v);
                animator.start();

                //找到第一个可见元素的位置
                int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                //如果没有元素，则从第0位插入元素
                if (position == RecyclerView.NO_POSITION) {
                    position = 0;
                }
                if (recyclerViewAdapter.getItemCount() != C.NAMES.length && isAdd) {//插入元素
                    //添加一个元素
                    addData(position);
                    //通知Adapter，position这里添加了一个元素，动态的把它显示出来
                    recyclerViewAdapter.notifyItemInserted(position);
                } else {//删除元素
                    //删除一个元素
                    removeData(position);
                    //通知Adapter，position位置的元素没了，动态的把它抹掉
                    recyclerViewAdapter.notifyItemRemoved(position);
                }

                //调整增减状态，即当前点击浮动按钮是应该添加元素，还是删除元素
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

    /**
     * 进入DetailActivity
     */
    public void startActivity(final View v, final int position) {
        //获取点击的Item，也是与DetailActivity的共享元素
        View pic = v.findViewById(R.id.img_list_item_pic);
        //共享元素壁纸的TransitionName
        String picTransitionName =  position + "pic";
        //声明使用的共享元素
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, Pair.create(pic,picTransitionName), Pair.create((View) imgBtnFloatButton, imgBtnFloatButton.getTransitionName())).toBundle();

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
