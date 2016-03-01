package com.bigfat.gankio_ca.presentation.ui.meizhi.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bigfat.gankio_ca.data.entity.GankEntity;
import com.bigfat.gankio_ca.presentation.R;
import com.bigfat.gankio_ca.presentation.common.ui.BaseRecyclerViewAdapter;
import com.bigfat.gankio_ca.presentation.util.GlideUtil;
import com.bigfat.gankio_ca.presentation.widget.RatioImageView;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import java.util.Collection;
import java.util.List;

/**
 * Created by yueban on 16:40 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class MeizhiListAdapter extends BaseRecyclerViewAdapter<GankEntity, MeizhiListAdapter.ViewHolder> {

    public MeizhiListAdapter(Context context, List<GankEntity> data) {
        super(context, data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflateItemView(parent, R.layout.item_gank));
    }

    @Override
    protected void bindDataToItemView(final MeizhiListAdapter.ViewHolder holder, final GankEntity gankEntity) {
        holder.mIvBg.setOriginalSize(gankEntity.getWidth(), gankEntity.getHeight());
        holder.itemView.setTag(gankEntity.getUrl());
        GlideUtil
            .url(mContext)
            .load(gankEntity.getUrl())
            .listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                    boolean isFromMemoryCache,
                    boolean isFirstResource) {

                    int width = resource.getIntrinsicWidth();
                    int height = resource.getIntrinsicHeight();

                    gankEntity.setWidth(width);
                    gankEntity.setHeight(height);
                    if (holder.itemView.getTag().equals(gankEntity.getUrl())) {
                        holder.mIvBg.setOriginalSize(width, height);
                        holder.mIvBg.requestLayout();
                    }

                    return false;
                }
            })
            .into(holder.mIvBg);
        holder.mTvDate.setText(gankEntity.getDesc());
    }

    public void setData(Collection<GankEntity> data) {
        mList.clear();
        addData(data);
    }

    public void addData(Collection<GankEntity> data) {
        mList.addAll(data);
        notifyDataSetChanged();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_bg)
        RatioImageView mIvBg;
        @Bind(R.id.tv_date)
        TextView mTvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
