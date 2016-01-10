package com.bigfat.materialviewpagertest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yueban on 19/8/15.
 */
public class TabViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public TabViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(android.R.id.text1);
    }
}
