package com.bigfat.listviewdragdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yueban on 16/7/15.
 */
public class SimpleListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> data;
    private int dragPosition = -1;
    private ListView listView;

    public SimpleListViewAdapter(Context context, ArrayList<String> data, ListView listView) {
        this.context = context;
        this.data = data;
        this.listView = listView;
    }

    public void setDragPosition(int dragPosition) {
        this.dragPosition = dragPosition;
    }

    public ArrayList<String> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.activity_list_item, parent, false);
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setText(data.get(position));
//        MainActivity.setupDragSort(view, listView);
        if (dragPosition == position) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
