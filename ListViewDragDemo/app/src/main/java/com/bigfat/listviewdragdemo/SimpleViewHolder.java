package com.bigfat.listviewdragdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class SimpleViewHolder extends RecyclerView.ViewHolder {
    public View itemView;
    public TextView tv;

    public SimpleViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.tv = (TextView) itemView.findViewById(android.R.id.text1);
    }
}