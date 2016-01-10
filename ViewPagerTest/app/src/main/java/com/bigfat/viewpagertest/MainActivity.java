package com.bigfat.viewpagertest;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        final List<View> viewList = new ArrayList<>();
        viewList.add(generateLinearLayout(20));
        viewList.add(generateLinearLayout(10));
        viewList.add(generateLinearLayout(30));
        viewList.add(generateLinearLayout(40));

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }
        });
    }

    private LinearLayout generateLinearLayout(int itemCount) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < itemCount; i++) {
            TextView textView = new TextView(this);
            textView.setTextSize(20);
            textView.setPadding(30, 30, 30, 30);
            textView.setText(String.valueOf(i));
            linearLayout.addView(textView);
        }
        return linearLayout;
    }
}
