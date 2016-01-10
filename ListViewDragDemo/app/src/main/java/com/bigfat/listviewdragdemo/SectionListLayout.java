package com.bigfat.listviewdragdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yueban on 16/7/15.
 */
public class SectionListLayout extends LinearLayout {
    private ArrayList<String> data;
    private int dragPosition = -1;

    public SectionListLayout(Context context) {
        this(context, null);
    }

    public SectionListLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        initView();
    }

    private void initView() {
        removeAllViews();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                View sectionView = generateItemView();
                if (dragPosition == i) {
                    sectionView.setVisibility(INVISIBLE);
                }
                TextView tv = (TextView) sectionView.findViewById(android.R.id.text1);
                tv.setText(data.get(i));
                addView(sectionView);
            }
        }
    }

    private View generateItemView() {
        return LayoutInflater.from(getContext()).inflate(android.R.layout.activity_list_item, this, false);
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
        initView();
    }

    public void notifyDataRemoved(int position) {
        removeViewAt(position);
    }

    public View notifyDataInserted(int position) {
        View sectionView = generateItemView();
        TextView tv = (TextView) sectionView.findViewById(android.R.id.text1);
        tv.setText(data.get(position));
        addView(sectionView, position);
        MainActivity.setupDragSort(sectionView, this);
        return sectionView;
    }

    public void setDragPosition(int dragPosition) {
        this.dragPosition = dragPosition;
    }

    public void refreshVisibility() {
        for (int i = 0; i < getChildCount(); i++) {
            if (dragPosition == i) {
                getChildAt(i).setVisibility(INVISIBLE);
            } else {
                getChildAt(i).setVisibility(VISIBLE);
            }
        }
    }
}
