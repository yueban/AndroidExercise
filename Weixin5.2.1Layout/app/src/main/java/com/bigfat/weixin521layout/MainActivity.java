package com.bigfat.weixin521layout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;

    private LinearLayout bannerLlChat;
    private LinearLayout bannerLlFind;
    private LinearLayout bannerLlContact;
    private TextView bannerTvChat;
    private TextView bannerTvFind;
    private TextView bannerTvContact;
    private BadgeView bannerBvChat;
    private BadgeView bannerBvFind;
    private BadgeView bannerBvContact;
    private ImageView bannerTabline;

    private int screenWidth1_3;//屏幕宽1/3
    private int mCurrentPageIndex;//当前选中页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTabLine();
        initView();
    }

    private void initTabLine() {
        bannerTabline = (ImageView) findViewById(R.id.banner_tabline);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth1_3 = outMetrics.widthPixels / 3;

        ViewGroup.LayoutParams lp = bannerTabline.getLayoutParams();
        lp.width = screenWidth1_3;
        bannerTabline.setLayoutParams(lp);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        bannerLlChat = (LinearLayout) findViewById(R.id.banner_ll_chat);
        bannerLlFind = (LinearLayout) findViewById(R.id.banner_ll_find);
        bannerLlContact = (LinearLayout) findViewById(R.id.banner_ll_contact);
        bannerTvChat = (TextView) findViewById(R.id.banner_tv_chat);
        bannerTvFind = (TextView) findViewById(R.id.banner_tv_find);
        bannerTvContact = (TextView) findViewById(R.id.banner_tv_contact);
        mDatas = new ArrayList<>();

        ChatMainTabFragment chatMainTabFragment = new ChatMainTabFragment();
        FindMainTabFragment findMainTabFragment = new FindMainTabFragment();
        ContactMainTabFragment contactMainTabFragment = new ContactMainTabFragment();
        mDatas.add(chatMainTabFragment);
        mDatas.add(findMainTabFragment);
        mDatas.add(contactMainTabFragment);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mDatas.get(position);
            }

            @Override
            public int getCount() {
                return mDatas.size();
            }
        };
        mViewPager.setAdapter(mAdapter);

        //设置监听器
        bannerLlChat.setOnClickListener(this);
        bannerLlFind.setOnClickListener(this);
        bannerLlContact.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bannerTabline.getLayoutParams();
                if (mCurrentPageIndex == 0 && position == 0) {//0->0
                    lp.leftMargin = (int) (screenWidth1_3 * positionOffset);
                } else if (mCurrentPageIndex == 1 && position == 0) {//1->0
                    lp.leftMargin = (int) (screenWidth1_3 * positionOffset);
                } else if (mCurrentPageIndex == 1 && position == 1) {//1->2
                    lp.leftMargin = (int) (screenWidth1_3 * (positionOffset + 1));
                } else if (mCurrentPageIndex == 2 && position == 1) {//2->1
                    lp.leftMargin = (int) (screenWidth1_3 * (positionOffset + 1));
                }
                bannerTabline.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPageIndex = position;
                resetTextView();
                switch (position) {
                    case 0:
                        bannerTvChat.setTextColor(0xFF008000);
                        if (bannerBvChat == null) {
                            bannerBvChat = new BadgeView(MainActivity.this);
                            bannerBvChat.setBadgeCount(10);
                            bannerLlChat.addView(bannerBvChat);
                        }
                        break;

                    case 1:
                        bannerTvFind.setTextColor(0xFF008000);
                        if (bannerBvFind == null) {
                            bannerBvFind = new BadgeView(MainActivity.this);
                            bannerBvFind.setBadgeCount(2);
                            bannerLlFind.addView(bannerBvFind);
                        }
                        break;

                    case 2:
                        bannerTvContact.setTextColor(0xFF008000);
                        if (bannerBvContact == null) {
                            bannerBvContact = new BadgeView(MainActivity.this);
                            bannerBvContact.setBadgeCount(5);
                            bannerLlContact.addView(bannerBvContact);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner_ll_chat:
                mViewPager.setCurrentItem(0);
                break;

            case R.id.banner_ll_find:
                mViewPager.setCurrentItem(1);
                break;

            case R.id.banner_ll_contact:
                mViewPager.setCurrentItem(2);
                break;
        }
    }

    private void resetTextView() {
        bannerTvChat.setTextColor(Color.BLACK);
        bannerTvFind.setTextColor(Color.BLACK);
        bannerTvContact.setTextColor(Color.BLACK);
    }
}
