package com.bigfat.android50test;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/1/22
 */
public class SampleRecyclerAdapter extends RecyclerView.Adapter<SampleRecyclerAdapter.ViewHolder> {

    private final ArrayList<SampleModel> sampleData = DemoApp.getSampleData(20);

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //获得列表项控件
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_basic_item, viewGroup, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final SampleModel rowData = sampleData.get(i);
        viewHolder.textViewSample.setText(rowData.getTitle());
        viewHolder.textViewSample.setTag(rowData);
    }

    public void removeData(int position) {
        sampleData.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(int position) {
        sampleData.add(position, new SampleModel("新的列表项" + new Random().nextInt(1000)));
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewSample;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewSample = (TextView) itemView.findViewById(R.id.textViewSample);
        }
    }
}
