package com.bigfat.listviewdragdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<SimpleViewHolder> {
    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<String> data;
    private int dragPosition = -1;

    public SimpleRecyclerViewAdapter(Context context, RecyclerView recyclerView, ArrayList<String> data) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.data = data;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setDragPosition(int dragPosition) {
        this.dragPosition = dragPosition;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.activity_list_item, viewGroup, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder simpleViewHolder, int i) {
        if (i == dragPosition) {
            simpleViewHolder.itemView.setVisibility(View.INVISIBLE);
        } else {
            simpleViewHolder.itemView.setVisibility(View.VISIBLE);
        }
//        DragUtils.setupDragSort(simpleViewHolder.itemView, recyclerView);
        simpleViewHolder.tv.setText(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}