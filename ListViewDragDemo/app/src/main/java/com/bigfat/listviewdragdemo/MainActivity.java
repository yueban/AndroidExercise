package com.bigfat.listviewdragdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

//    private MyHorizontalScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_section_scroll_view);

//        scrollView = (MyHorizontalScrollView) findViewById(R.id.scrollView);
//        scrollView.setOnPageSelectListener(new MyHorizontalScrollView.OnPageSelectListener() {
//            @Override
//            public void onPageSelect(int currentPage) {
//
//            }
//        });
//        scrollView.setData(generateData());
        ScrollView scrollView = (ScrollView) findViewById(R.id.sv_item_section);

        SectionListLayout layout = new SectionListLayout(this);
        layout.setData(generateStringList("", 40));
        scrollView.addView(layout);

        for(int i = 0;i < layout.getChildCount();i++){
            setupDragSort(layout.getChildAt(i), layout);
        }
    }

    private ArrayList<Section> generateData() {
        ArrayList<Section> data = new ArrayList<>();

        data.add(new Section("阶段1", generateStringList("1-", 20)));
        data.add(new Section("阶段2", generateStringList("2-", 10)));
        data.add(new Section("阶段3", generateStringList("3-", 35)));
        data.add(new Section("阶段4", generateStringList("4-", 50)));

        return data;
    }

    private ArrayList<String> generateStringList(String title, int size) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(title + i);
        }
        return data;
    }

   private static android.os.Handler handler = new android.os.Handler();

    private static Runnable runnable;

    public static void setupDragSort( View view, final SectionListLayout layout) {
        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(final View view, final DragEvent event) {
                final ViewGroup viewGroup = (ViewGroup) view.getParent();
                final DragUtils.DragState dragState = (DragUtils.DragState) event.getLocalState();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        if (view == dragState.view) {
                            dragState.view.setVisibility(View.INVISIBLE);
                        }
                        if (runnable != null) {
                            handler.removeCallbacks(runnable);
                            runnable = null;
                        }
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        if (view != dragState.view) {
                            final int position = layout.indexOfChild(view);

                            //拖拽到达列表边界时，令ScrollView滚动
                            ScrollView svSection = (ScrollView) viewGroup.getParent();
                            if ((position + 2) * view.getHeight() > svSection.getHeight() + svSection.getScrollY()) {
                                svSection.smoothScrollBy(0, view.getHeight());
                            } else if (position * view.getHeight() < svSection.getScrollY()) {
                                svSection.smoothScrollBy(0, -view.getHeight());
                            }

                            //处理View拖拽交换
                            if (runnable != null) {
                                handler.removeCallbacks(runnable);
                                runnable = null;
                            }
                            runnable = new Runnable() {
                                @Override
                                public void run() {
                                    swipeViews(dragState, (SectionListLayout) viewGroup, position);
                                }
                            };
                            handler.postDelayed(runnable, DragUtils.itemExchangeDelay);
                        }
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.i(TAG, "ACTION_DRAG_ENDED");
                        if (runnable != null) {
                            handler.removeCallbacks(runnable);
                            runnable = null;
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                dragState.view.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                }
                return true;
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.startDrag(null, new View.DragShadowBuilder(view), new DragUtils.DragState(view, DragUtils.DragViewType.TASK, layout.indexOfChild(view)), 0);
                return true;
            }
        });
    }

    private static void swipeViews(DragUtils.DragState dragState, SectionListLayout layoutInserted, int insertPosition) {
        SectionListLayout layoutRemoved = (SectionListLayout) dragState.view.getParent();

        String text =layoutRemoved.getData().get(dragState.position);
        layoutRemoved.getData().remove(dragState.position);
        layoutRemoved.notifyDataRemoved(dragState.position);

        layoutInserted.getData().add(insertPosition, text);
        dragState.view = layoutInserted.notifyDataInserted(insertPosition);

        layoutRemoved.setDragPosition(-1);
        layoutInserted.setDragPosition(insertPosition);
        layoutInserted.refreshVisibility();
        dragState.position = insertPosition;
    }
}