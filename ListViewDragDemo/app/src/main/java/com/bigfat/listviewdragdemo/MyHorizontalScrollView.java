package com.bigfat.listviewdragdemo;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yueban on 10/7/15.
 */
public class MyHorizontalScrollView extends HorizontalScrollView {
    public static final String TAG = MyHorizontalScrollView.class.getSimpleName();

    //控件
    private MyHorizontalLinearLayout mainLayout;//布局控件

    private ArrayList<Section> data;//数据
    private int currentPage = 0;//当前页Index
    private OnPageSelectListener onPageSelectListener;

    public MyHorizontalScrollView(Context context) {
        this(context, null);
    }


    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setData(ArrayList<Section> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            View sectionView = LayoutInflater.from(getContext()).inflate(R.layout.item_section_recycler_view, mainLayout, false);
            TextView tvTitle = (TextView) sectionView.findViewById(R.id.tv_item_section_title);
            RecyclerView recyclerView = (RecyclerView) sectionView.findViewById(R.id.rv_item_section);

            Section section = data.get(i);
            tvTitle.setText(section.getTitle());
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            SimpleRecyclerViewAdapter adapter = new SimpleRecyclerViewAdapter(getContext(), recyclerView, section.getData());
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);

            mainLayout.addView(sectionView);
        }
        initDragEvent();
    }

    public MyHorizontalLinearLayout getMainLayout() {
        return mainLayout;
    }

    public void initDragEvent() {
        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            ViewGroup viewGroup = (ViewGroup) mainLayout.getChildAt(i);
//            DragUtils.setupDragSortHorizontal(this, viewGroup, DragUtils.DragViewType.SECTION);

            final RecyclerView recyclerView = (RecyclerView) viewGroup.findViewById(R.id.rv_item_section);
            final RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < manager.getChildCount(); j++) {
                        DragUtils.setupDragSort(MyHorizontalScrollView.this, manager.getChildAt(j), recyclerView);
                    }
                }
            });
        }
    }

    public void removeDragEvent(DragUtils.DragViewType type) {
        switch (type) {
            case SECTION:
                for (int i = 0; i < mainLayout.getChildCount(); i++) {
                    ViewGroup viewGroup = (ViewGroup) mainLayout.getChildAt(i);
                    viewGroup.setOnDragListener(null);
                    viewGroup.setOnLongClickListener(null);
                }
                break;

            case TASK:
//                for (int i = 0; i < mainLayout.getChildCount(); i++) {
//                    ViewGroup viewGroup = (ViewGroup) mainLayout.getChildAt(i);
//                    LinearLayout linearLayout = (LinearLayout) viewGroup.findViewById(R.id.ll_item_section);
//                    for (int j = 0; j < linearLayout.getChildCount(); j++) {
//                        View view = linearLayout.getChildAt(j);
//                        view.setOnDragListener(null);
//                        view.setOnLongClickListener(null);
//                    }
//                }
                break;
        }
    }

    public void setOnPageSelectListener(OnPageSelectListener onPageSelectListener) {
        this.onPageSelectListener = onPageSelectListener;
    }

    private void initView() {
        setHorizontalScrollBarEnabled(false);
        mainLayout = new MyHorizontalLinearLayout(getContext());
        addView(mainLayout);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollToPage(currentPage);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                currentPage = (int) Math.round(getScrollX() * 1.0 / DragUtils.pageScrollWidth);
                scrollToCurrentPage();
                return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滚动到当前页
     */
    public void scrollToCurrentPage() {
        Log.i(TAG, "scrollToCurrentPage");
        smoothScrollTo(currentPage * DragUtils.pageScrollWidth, 0);
        if (onPageSelectListener != null) {
            onPageSelectListener.onPageSelect(currentPage);
        }
    }

    /**
     * 滚动到上一页
     */
    public void scrollToPreviousPage() {
        Log.i(TAG, "scrollToPreviousPage");
        if (currentPage > 0) {//不是第一页
            currentPage--;
            scrollToCurrentPage();
        }
    }

    /**
     * 滚动到下一页
     */
    public void scrollToNextPage() {
        Log.i(TAG, "scrollToNextPage");
        if (currentPage < ((ViewGroup) getChildAt(0)).getChildCount() - 1) {//不是最后一页
            currentPage++;
            scrollToCurrentPage();
        }
    }

    /**
     * 滚动到指定页
     *
     * @param pageIndex 指定页索引
     */
    public void scrollToPage(int pageIndex) {
        Log.i(TAG, "scrollToPage");
        if (pageIndex >= 0 && pageIndex <= ((ViewGroup) getChildAt(0)).getChildCount() - 1) {//指定页索引在范围内
            currentPage = pageIndex;
            scrollToCurrentPage();
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public interface OnPageSelectListener {
        void onPageSelect(int currentPage);
    }
}