package com.juphoon.cloud.doodle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ColorSelector extends LinearLayout implements View.OnClickListener {
    public interface ColorListener {
        void onColorSelected(int color);
    }

    public static final int COLUMN_NUMBER = 5;
    private int[] mColors;
    private ColorListener mColorListener;

    public ColorSelector(Context context) {
        this(context, null);
    }

    public ColorSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initColors(int[] colors, int selectedIndex) {
        mColors = colors;
        int radius = getResources().getDimensionPixelSize(R.dimen.color_palette_radius);
        int width = getResources().getDimensionPixelSize(R.dimen.color_selector_width);
        int colorLength = mColors.length;
        int rowNumber = colorLength / COLUMN_NUMBER + (colorLength % COLUMN_NUMBER == 0 ? 0 : 1);
        DotView selectedView = null;
        for (int i = 0; i < rowNumber; i++) {
            LinearLayout rowLayout = new LinearLayout(getContext());
            for (int n = 0; n < COLUMN_NUMBER; n++) {
                int index = COLUMN_NUMBER * i + n;
                DotView view = new DotView(getContext(), mColors[index], radius);
                @SuppressWarnings("SuspiciousNameCombination") LayoutParams layoutParams = new LayoutParams(width, width);
                view.setLayoutParams(layoutParams);
                if (index == selectedIndex) {
                    selectedView = view;
                }
                view.setOnClickListener(this);
                rowLayout.addView(view);
            }
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rowLayout.setLayoutParams(params);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            addView(rowLayout);
        }

        if (selectedView != null) {
            selectedView.setImageResource(R.drawable.doodle_select_color_border_shape);
        }
    }

    public void setColorListener(ColorListener listener) {
        mColorListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (!(v instanceof DotView)) {
            return;
        }
        int color = 0;
        int colorLength = mColors.length;
        int rowNumber = colorLength / COLUMN_NUMBER + (colorLength % COLUMN_NUMBER == 0 ? 0 : 1);
        DotView dotView;
        for (int i = 0; i < rowNumber; i++) {
            LinearLayout layout = (LinearLayout) getChildAt(i);
            for (int n = 0; n < COLUMN_NUMBER; n++) {
                dotView = (DotView) layout.getChildAt(n);
                if (v == dotView) {
                    dotView.setImageResource(R.drawable.doodle_select_color_border_shape);
                    color = mColors[COLUMN_NUMBER * i + n];
                } else {
                    dotView.setImageResource(0);
                }
            }
        }
        if (mColorListener != null) {
            mColorListener.onColorSelected(color);
        }
    }
}
