package com.bigfat.androidltest;

import android.animation.Animator;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bigfat.androidltest.model.Paper;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/12
 */
public class DetailActivity extends Activity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageView imgPic;
    private ImageButton imgBtnFloatButton;
    private TextView tvName;
    private TextView tvWork;

    //主壁纸
    private Paper paper;
    //壁纸组当前索引
    private int picIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //开启转场动画功能
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //设置转场动画
        getWindow().setEnterTransition(new Explode().setDuration(500));
        getWindow().setExitTransition(null);

        setContentView(R.layout.activity_detail);

        int position = getIntent().getIntExtra("position", 0);
        paper = MainActivity.paperList.get(position);

        initView();
        initToolbar();
        initEvent();

        //为imgPic设置与前一场景共享元素相同的TransitionName，系统就是根据TransitionName为共享元素配对的
        imgPic.setTransitionName(position + "pic");
        //设置界面内容
        imgPic.setImageResource(paper.getPic());
        tvName.setText(paper.getName());
        tvWork.setText(paper.getWork());
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.tb_detail);
        imgPic = (ImageView) findViewById(R.id.img_detail_pic);
        imgBtnFloatButton = (ImageButton) findViewById(R.id.imgbtn_detail_float_button);
        tvName = (TextView) findViewById(R.id.tv_detail_name);
        tvWork = (TextView) findViewById(R.id.tv_detail_work);
    }

    private void initToolbar() {
        setActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            getActionBar().setTitle(paper.getName());
            actionBar.setSubtitle(paper.getWork());
        }
    }

    private void initEvent() {
        imgBtnFloatButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_detail_float_button://点击后切换当前组下一张壁纸
                picIndex++;
                //获取并设置壁纸
                if (paper.getPicGroup() != null) {
                    if (picIndex > paper.getPicGroup().length - 1) {
                        picIndex = 0;
                    }
                    imgPic.setImageResource(paper.getPicGroup()[picIndex]);
                }
                //壁纸切换动画
                Animator animator = createCircularRevealAnimatorRightBottom(imgPic);
                animator.start();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //返回键点击后将壁纸图片还原，要不然和MainActivity的共享元素图片不一样，过渡太违和了
        imgPic.setImageResource(paper.getPic());
        finishAfterTransition();
    }

    /**
     * 获取壁纸图片切换动画
     */
    private Animator createCircularRevealAnimatorRightBottom(View v) {
        Animator animator = ViewAnimationUtils.createCircularReveal(v, imgBtnFloatButton.getLeft() + imgBtnFloatButton.getWidth() / 2, imgBtnFloatButton.getTop() + imgBtnFloatButton.getHeight() / 2, 0, v.getWidth());
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);
        return animator;
    }
}
