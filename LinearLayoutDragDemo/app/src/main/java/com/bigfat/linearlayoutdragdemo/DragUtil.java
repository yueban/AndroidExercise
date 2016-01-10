package com.bigfat.linearlayoutdragdemo;

import android.animation.ObjectAnimator;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by yueban on 10/9/15.
 */
public class DragUtil {
    public static void setupDragSort(View view) {
        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(final View view, DragEvent event) {
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                DragState dragState = (DragState) event.getLocalState();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        if (view == dragState.view) {
                            view.setVisibility(View.INVISIBLE);
                        }
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        if (view == dragState.view) {
                            break;
                        }
                        int index = viewGroup.indexOfChild(view);
                        if ((index > dragState.index && event.getY() > view.getHeight() / 2)
                                || (index < dragState.index && event.getY() < view.getHeight() / 2)) {
                            swapViews(viewGroup, view, index, dragState);
                        }
                        break;

                    case DragEvent.ACTION_DROP:
                        if (view != dragState.view) {
                            swapViewGroupChildren(viewGroup, view, dragState.view);
                        }
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        if (view == dragState.view) {
                            view.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                return true;
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.startDrag(null, new View.DragShadowBuilder(view), new DragState(view), 0);
                return true;
            }
        });
    }

    private static void swapViewsBetweenIfNeeded(ViewGroup viewGroup, int index, DragState dragState) {
        if (index - dragState.index > 1) {
            int indexAbove = index - 1;
            swapViews(viewGroup, viewGroup.getChildAt(indexAbove), indexAbove, dragState);
        } else if (dragState.index - index > 1) {
            int indexBelow = index + 1;
            swapViews(viewGroup, viewGroup.getChildAt(indexBelow), indexBelow, dragState);
        }
    }

    private static void swapViews(ViewGroup viewGroup, final View view, int index, DragState dragState) {
        swapViewsBetweenIfNeeded(viewGroup, index, dragState);
        final float viewY = view.getY();
        swapViewGroupChildren(viewGroup, view, dragState.view);
        dragState.index = index;
        postOnPreDraw(view, new Runnable() {
            @Override
            public void run() {
                ObjectAnimator
                        .ofFloat(view, View.Y, viewY, view.getTop())
                        .setDuration(200)
                        .start();
            }
        });
    }

    private static void postOnPreDraw(View view, final Runnable runnable) {
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

    private static void swapViewGroupChildren(ViewGroup viewGroup, View firstView, View secondView) {
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

    private static class DragState {
        public View view;
        public int index;

        private DragState(View view) {
            this.view = view;
            index = ((ViewGroup) view.getParent()).indexOfChild(view);
        }
    }
}
