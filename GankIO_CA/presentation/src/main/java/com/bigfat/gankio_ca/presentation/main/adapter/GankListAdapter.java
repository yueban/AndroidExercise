package com.bigfat.gankio_ca.presentation.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bigfat.gankio_ca.domain.entity.GankEntity;
import com.bigfat.gankio_ca.presentation.R;
import com.bigfat.gankio_ca.presentation.util.GlideUtil;
import com.bigfat.gankio_ca.presentation.widget.RatioImageView;
import java.util.Collection;
import java.util.List;

/**
 * Created by yueban on 16:40 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class GankListAdapter extends RecyclerView.Adapter<GankListAdapter.ViewHolder> {
    private Context mContext;
    private List<GankEntity> mData;

    public GankListAdapter(Context context, List<GankEntity> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GankEntity gankEntity = mData.get(position);
        GlideUtil
            .url(mContext)
            .load(gankEntity.getUrl())
            .centerCrop()
            .into(holder.mIvBg);
        holder.mTvDate.setText(gankEntity.getDesc());
    }

    public void setData(Collection<GankEntity> data) {
        mData = (List<GankEntity>) data;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_bg)
        RatioImageView mIvBg;
        @Bind(R.id.tv_date)
        TextView mTvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mIvBg.setOriginalSize(50, 50);
        }
    }
}
