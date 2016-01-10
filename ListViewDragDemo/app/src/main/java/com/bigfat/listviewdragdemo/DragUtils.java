package com.bigfat.listviewdragdemo;

/*
 * Copyright (c) 2014-2015 Zhang Hai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.animation.ObjectAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

// TODO: Provide scrolling at edge, see
// https://github.com/justasm/DragLinearLayout/blob/master/library/src/main/java/com/jmedeisis/draglinearlayout/DragLinearLayout.java
// and
// https://github.com/nhaarman/ListViewAnimations/blob/master/lib-manipulation/src/main/java/com/nhaarman/listviewanimations/itemmanipulation/dragdrop/DragAndDropHandler.java
public class DragUtils {

    private static final String TAG = DragUtils.class.getSimpleName();
    public static int screenWidth;
    public static int screenHeight;
    public static int pageEdgeVisibleWidth;//非当前page边缘可见宽度
    public static int pageMargin;//每一页左/右边距
    public static int pageScrollWidth;//滚动时每一页的滚动距离
    public static int pageExchangeDelay = 800;//拖拽至边界时切换页面的响应延迟
    public static int itemExchangeDelay = 500;//item切换响应延迟
    private static Runnable runnable = null;

    static {
        screenWidth = App.getContext().getResources().getDisplayMetrics().widthPixels;
        screenHeight = App.getContext().getResources().getDisplayMetrics().heightPixels;
        pageEdgeVisibleWidth = App.getContext().getResources().getDimensionPixelOffset(R.dimen.page_edge_visible_width);
        pageMargin = App.getContext().getResources().getDimensionPixelOffset(R.dimen.page_margin);
        pageScrollWidth = screenWidth - pageMargin * 2 - pageEdgeVisibleWidth * 2;
    }

    private DragUtils() {
    }

    public static void setupDragSort(final MyHorizontalScrollView scrollView, View view, final RecyclerView recyclerView) {
        final SimpleRecyclerViewAdapter adapter = (SimpleRecyclerViewAdapter) recyclerView.getAdapter();
        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final ArrayList<String> data = adapter.getData();

        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(final View view, final DragEvent event) {
                final ViewGroup viewGroup = (ViewGroup) view.getParent();
                final DragState dragState = (DragState) event.getLocalState();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        if (view == dragState.view) {
                            adapter.setDragPosition(viewGroup.indexOfChild(view));
                            dragState.view.setVisibility(View.INVISIBLE);
                        }
                        if (runnable != null) {
                            recyclerView.removeCallbacks(runnable);
                            runnable = null;
                        }
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        if (view != dragState.view) {
                            final int position = manager.getPosition(view);

                            //拖拽到达列表边界时，令RecyclerView滚动
                            if (position >= manager.findLastCompletelyVisibleItemPosition() - 1) {//向下滚
                                recyclerView.scrollBy(0, view.getHeight());
                            } else if (position <= manager.findFirstCompletelyVisibleItemPosition() + 1) {//向上滚
                                recyclerView.scrollBy(0, -view.getHeight());
                            }

                            //处理View拖拽交换
                            if (runnable != null) {
                                recyclerView.removeCallbacks(runnable);
                                runnable = null;
                            }
                            runnable = new Runnable() {
                                @Override
                                public void run() {
//                                    Log.i(TAG, "position--->" + position);
//                                    Log.i(TAG, "dragState.position--->" + dragState.position);
                                    String text = data.get(dragState.position);
                                    data.remove(dragState.position);
                                    data.add(position, text);
                                    adapter.setDragPosition(position);
                                    adapter.notifyItemRemoved(dragState.position);
                                    adapter.notifyItemInserted(position);
                                    if (position == 0) {
                                        recyclerView.scrollToPosition(0);
                                    }
                                    dragState.position = position;
                                }
                            };
                            recyclerView.postDelayed(runnable, itemExchangeDelay);
                        }
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.i(TAG, "ACTION_DRAG_ENDED");
                        if (runnable != null) {
                            recyclerView.removeCallbacks(runnable);
                            runnable = null;
                        }
                        adapter.setDragPosition(-1);
                        adapter.notifyDataSetChanged();
                        break;
                }
                return true;
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.startDrag(null, new View.DragShadowBuilder(view), new DragState(view, DragViewType.TASK, manager.getPosition(view)), 0);
                scrollView.removeDragEvent(DragViewType.SECTION);
                return true;
            }
        });
    }

    public static void setupDragSortHorizontal(final MyHorizontalScrollView scrollView, View view, final DragViewType type) {
        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(final View view, final DragEvent event) {
                final ViewGroup viewGroup = (ViewGroup) view.getParent();
                final DragState dragState = (DragState) event.getLocalState();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        if (view == dragState.view) {
                            view.setVisibility(View.INVISIBLE);
                        }
                        if (runnable != null) {
                            scrollView.removeCallbacks(runnable);
                            runnable = null;
                        }
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        if (view == dragState.view) {
                            if ((event.getX() > view.getWidth() / 8 * 7)
                                    || (event.getX() < view.getWidth() / 8)) {
                                final int index = event.getX() > view.getWidth() / 2 ? viewGroup.indexOfChild(view) + 1 : viewGroup.indexOfChild(view) - 1;
                                if (viewGroup.getChildAt(index) != null) {
                                    if (runnable == null) {
                                        runnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.i(TAG, "runnable--->scrollToPage--->" + index);
                                                scrollView.scrollToPage(index);
                                                scrollView.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        swapHorizontalViews(viewGroup, viewGroup.getChildAt(index), index, dragState);
                                                    }
                                                }, 50);
                                                runnable = null;
                                            }
                                        };
                                        scrollView.postDelayed(runnable, pageExchangeDelay);
                                    }
                                }
                            } else if (runnable != null) {
                                scrollView.removeCallbacks(runnable);
                                runnable = null;
                            }
                        }
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        if (view == dragState.view) {
                            view.setVisibility(View.VISIBLE);
                        }
                        if (runnable != null) {
                            scrollView.removeCallbacks(runnable);
                            runnable = null;
                        }
                        scrollView.initDragEvent();
                        break;
                }
                return true;
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                scrollView.removeDragEvent(DragViewType.TASK);
                view.startDrag(null, new View.DragShadowBuilder(view), new DragState(view, type, ((ViewGroup) view.getParent()).indexOfChild(view)), 0);
                return true;
            }
        });
    }

    public static int getDuration(View view) {
        return view.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    public static void postOnPreDraw(View view, final Runnable runnable) {
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }
                runnable.run();
                return true;
            }
        });
    }

    public static void swapViewGroupChildren(ViewGroup firstGroup, ViewGroup secondGroup, View firstView, View secondView) {
        int firstIndex = firstGroup.indexOfChild(firstView);
        int secondIndex = secondGroup.indexOfChild(secondView);
        if (firstIndex < secondIndex) {
            secondGroup.removeViewAt(secondIndex);
            firstGroup.removeViewAt(firstIndex);
            firstGroup.addView(secondView, firstIndex);
            secondGroup.addView(firstView, secondIndex);
        } else {
            firstGroup.removeViewAt(firstIndex);
            secondGroup.removeViewAt(secondIndex);
            secondGroup.addView(firstView, secondIndex);
            firstGroup.addView(secondView, firstIndex);
        }
    }

    private static void swapHorizontalViews(ViewGroup viewGroup, final View view, int position,
                                            DragUtils.DragState dragState) {
        final float viewX = view.getX();
        swapHorizontalViewGroupChildren(viewGroup, view, dragState.view);
        dragState.position = position;
        DragUtils.postOnPreDraw(view, new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animator = ObjectAnimator
                        .ofFloat(view, View.X, viewX, view.getLeft())
                        .setDuration(DragUtils.getDuration(view));
                animator.setInterpolator(new LinearInterpolator());
                animator.start();
            }
        });
    }

    public static void swapHorizontalViewGroupChildren(ViewGroup viewGroup, View firstView, View secondView) {
        int firstIndex = viewGroup.indexOfChild(firstView);
        int secondIndex = viewGroup.indexOfChild(secondView);
        if (firstIndex < secondIndex) {
            viewGroup.removeViewAt(secondIndex);
            viewGroup.removeViewAt(firstIndex);
            viewGroup.addView(secondView, firstIndex);
            viewGroup.addView(firstView, secondIndex);
        } else {
            viewGroup.removeViewAt(firstIndex);
            viewGroup.removeViewAt(secondIndex);
            viewGroup.addView(firstView, secondIndex);
            viewGroup.addView(secondView, firstIndex);
        }
    }

    public enum DragViewType {
        SECTION,//阶段
        TASK,//任务
    }

    public static interface DragListener {
        public void onDragStarted();

        public void onDragEnded();
    }

    public static interface OnDragDeletedListener {
        public void onDragDeleted();
    }

    public static class DragState {
        public View view;
        public DragViewType type;
        public int position;

        public DragState(View view, DragViewType type, int position) {
            this.view = view;
            this.type = type;
            this.position = position;
        }
    }
}