package com.bigfat.itemtouchhelperdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yueban on 21:32 12/1/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextView;

    public ItemViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.text);
    }
}
