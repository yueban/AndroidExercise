package com.bigfat.weixin60ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private List<Fragment> mTabs = new ArrayList<>();
    private String[] mTitles = new String[]{
            "First Fragment!", "Second Fragment!", "Third Fragment!", "Fourth Fragment!"
    };

    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<>();//四个底部tab指示器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setOverflowButtonAlways();

        initView();
        initData();
        initEvent();

        viewPager.setAdapter(adapter);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ChangeColorIconWithText indicator1 = (ChangeColorIconWithText) findViewById(R.id.tab_indicator_1);
        mTabIndicators.add(indicator1);
        ChangeColorIconWithText indicator2 = (ChangeColorIconWithText) findViewById(R.id.tab_indicator_2);
        mTabIndicators.add(indicator2);
        ChangeColorIconWithText indicator3 = (ChangeColorIconWithText) findViewById(R.id.tab_indicator_3);
        mTabIndicators.add(indicator3);
        ChangeColorIconWithText indicator4 = (ChangeColorIconWithText) findViewById(R.id.tab_indicator_4);
        mTabIndicators.add(indicator4);

        indicator1.setOnClickListener(indicatorClickEvent);
        indicator2.setOnClickListener(indicatorClickEvent);
        indicator3.setOnClickListener(indicatorClickEvent);
        indicator4.setOnClickListener(indicatorClickEvent);

        indicator1.setIconAlpha(1.0f);
    }

    private void initData() {
        for (String title : mTitles) {
            TabFragment tabFragment = new TabFragment();
            Bundle b = new Bundle();
            b.putString(TabFragment.TITLE, title);
            tabFragment.setArguments(b);
            mTabs.add(tabFragment);
        }

        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }

            @Override
            public int getCount() {
                return mTabs.size();
            }
        };
    }

    private void initEvent() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    ChangeColorIconWithText left = mTabIndicators.get(position);
                    ChangeColorIconWithText right = mTabIndicators.get(position + 1);
                    left.setIconAlpha(1 - positionOffset);
                    right.setIconAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * TabIndicator点击事件
     */
    private View.OnClickListener indicatorClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetOtherTabIndicators();
            switch (v.getId()) {
                case R.id.tab_indicator_1:
                    mTabIndicators.get(0).setIconAlpha(1.0f);
                    viewPager.setCurrentItem(0, false);
                    break;
                case R.id.tab_indicator_2:
                    mTabIndicators.get(1).setIconAlpha(1.0f);
                    viewPager.setCurrentItem(1, false);
                    break;
                case R.id.tab_indicator_3:
                    mTabIndicators.get(2).setIconAlpha(1.0f);
                    viewPager.setCurrentItem(2, false);
                    break;
                case R.id.tab_indicator_4:
                    mTabIndicators.get(3).setIconAlpha(1.0f);
                    viewPager.setCurrentItem(3, false);
                    break;
            }
        }
    };

    /**
     * 重置其他TabIndicator的颜色
     */
    private void resetOtherTabIndicators() {
        for (ChangeColorIconWithText indicator : mTabIndicators) {
            indicator.setIconAlpha(0);
        }
    }

    /**
     * 即使有物理Menu键也显示ActionBar的flowMenu
     */
    private void setOverflowButtonAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 设置Menu显示icon
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
