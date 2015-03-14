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

    //当前壁纸
    private Paper paper;
    //壁纸组当前索引
    private int picIndex;
    private boolean isAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode().setDuration(1000));
//        getWindow().setEnterTransition(new Fade().setDuration(1000));
//        getWindow().setEnterTransition(new ChangeBounds().setDuration(1000));
        getWindow().setExitTransition(null);
        setContentView(R.layout.activity_detail);

        int position = getIntent().getIntExtra("position", 0);
        paper = MainActivity.paperList.get(position);

        initView();
        initToolbar();
        initEvent();

        imgPic.setTransitionName(position + "pic");
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
        if(actionBar !=null){
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
            case R.id.imgbtn_detail_float_button:
                if (!isAnim) {
                    Animator animator = createCircularRevealAnimator(imgPic, true);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isAnim = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            picIndex++;
                            if (paper.getPicGroup() != null) {
                                if (picIndex > paper.getPicGroup().length - 1) {
                                    picIndex = 0;
                                }
                                doSecondAnim();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.start();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        imgPic.setImageResource(paper.getPic());
        finishAfterTransition();
    }

    /**
     * 图片执行第二次动画，即显示动画
     */
    private void doSecondAnim() {
        imgPic.setImageResource(paper.getPicGroup()[picIndex]);
        Animator animator = createCircularRevealAnimator(imgPic, false);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnim = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    /**
     * 获取圆形切小放大动画
     *
     * @param v       要执行动画的View
     * @param isFirst 是否第一次动画，第一次动画是圆形渐没动画，第二次则是圆形渐显动画
     */
    private Animator createCircularRevealAnimator(View v, boolean isFirst) {
        Animator animator;

        if (isFirst) {
            animator = ViewAnimationUtils.createCircularReveal(v, v.getWidth() / 2, v.getHeight() / 2, v.getWidth(), 0);
        } else {
            animator = ViewAnimationUtils.createCircularReveal(v, v.getWidth() / 2, v.getHeight() / 2, 0, v.getWidth());
        }
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);

        return animator;
    }
}
