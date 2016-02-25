package com.bigfat.itemtouchhelperdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yueban on 21:36 12/1/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<ItemViewHolder> implements ItemTouchHelperAdapter {
    private final List<String> allItems;
    private List<String> mItems = new ArrayList<>();

    public RecyclerListAdapter(List<String> items) {
        allItems = items;
        mItems.addAll(allItems);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.mTextView.setText(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onDragStarted() {
        List<String> items = allItems.subList(0, 7);
        mItems.clear();
        mItems.addAll(items);
        notifyItemRangeRemoved(7, 23);
        mItems.remove(5);
        notifyItemRemoved(5);
        mItems.remove(3);
        notifyItemRemoved(3);
        mItems.remove(1);
        notifyItemRemoved(1);
    }

    @Override
    public void onDragEnded() {
        mItems.clear();
        mItems.addAll(allItems);
        notifyItemInserted(1);
        notifyItemInserted(3);
        notifyItemInserted(5);
        notifyItemRangeInserted(7, 23);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mItems, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mItems, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }
}
