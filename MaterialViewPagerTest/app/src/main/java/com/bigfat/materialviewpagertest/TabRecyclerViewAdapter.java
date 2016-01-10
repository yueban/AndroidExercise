package com.bigfat.materialviewpagertest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by yueban on 19/8/15.
 */
public class TabRecyclerViewAdapter extends RecyclerView.Adapter<TabViewHolder> {
    private Context context;
    private ArrayList<String> data;

    public TabRecyclerViewAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public TabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TabViewHolder(LayoutInflater.from(context).inflate(android.R.layout.activity_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TabViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
