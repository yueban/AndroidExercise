package com.bigfat.viewpagerinscrollviewdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity {

    private static final String[] titls = {"Guy", "Gay", "Buy"};
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.id_stickynavlayout_indicator);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return titls[position];
            }

            @Override
            public Fragment getItem(int position) {
                return TabFragment.newInstance(getPageTitle(position).toString());
            }

            @Override
            public int getCount() {
                return titls.length;
            }
        });
        tabs.setViewPager(viewPager);
    }
}
