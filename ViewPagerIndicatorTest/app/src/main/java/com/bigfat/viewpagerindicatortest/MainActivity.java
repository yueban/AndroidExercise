package com.bigfat.viewpagerindicatortest;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.viewpagerindicator.TabPageIndicator;


public class MainActivity extends ActionBarActivity {

    private ViewPager mViewPager;
    private TabPageIndicator mTabPageIndicator;
    private TabAdapter mTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewPager);
        mTabPageIndicator = (TabPageIndicator) findViewById(R.id.id_indicator);

        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabAdapter);
        mTabPageIndicator.setViewPager(mViewPager, 0);
    }
}
