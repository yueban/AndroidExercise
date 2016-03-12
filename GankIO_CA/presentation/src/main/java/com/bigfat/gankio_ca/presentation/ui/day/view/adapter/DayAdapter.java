package com.bigfat.gankio_ca.presentation.ui.day.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bigfat.gankio_ca.data.entity.DayEntity;
import com.bigfat.gankio_ca.presentation.R;

/**
 * Created by yueban on 00:08 2/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    private static final int TYPE_PIC = 1;
    private static final int TYPE_LABEL = 2;
    private static final int TYPE_ITEM = 3;

    private final Context mContext;
    private DayEntity mDayEntity;

    public DayAdapter(Context context, DayEntity dayEntity) {
        mContext = context;
        mDayEntity = dayEntity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        
        return super.getItemViewType(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_date) TextView mTvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
