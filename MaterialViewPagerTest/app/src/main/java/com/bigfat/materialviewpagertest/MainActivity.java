package com.bigfat.materialviewpagertest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.github.florent37.materialviewpager.MaterialViewPager;

public class MainActivity extends AppCompatActivity {
    private MaterialViewPager materialViewPager;
    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private String[] titles = {"评论", "任务日志", "文件"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        materialViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        viewPager = materialViewPager.getViewPager();

        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }

            @Override
            public Fragment getItem(int position) {
                return TabFragment.newInstance(titles[position]);
            }

            @Override
            public int getCount() {
                return titles.length;
            }
        };
        viewPager.setAdapter(adapter);
        materialViewPager.getPagerTitleStrip().setViewPager(viewPager);
    }
}
