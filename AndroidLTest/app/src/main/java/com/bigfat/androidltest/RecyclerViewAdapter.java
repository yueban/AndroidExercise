package com.bigfat.androidltest;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/11
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private String[] mData;

    public RecyclerViewAdapter(String[] mData) {
        this.mData = mData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(TextView itemView) {
            super(itemView);
            this.textView = itemView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        TextView tv = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(mData[i]);
        viewHolder.textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, v.getResources().getDisplayMetrics()));
                        break;

                    case MotionEvent.ACTION_UP:
                        v.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, v.getResources().getDisplayMetrics()));
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }
}
