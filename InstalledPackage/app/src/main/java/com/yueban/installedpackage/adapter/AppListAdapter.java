package com.yueban.installedpackage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.yueban.installedpackage.entity.AppEntity;
import com.yueban.installedpackage.viewholder.AppViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yueban
 * @date 2017/1/17
 * @email fbzhh007@gmail.com
 */
public class AppListAdapter extends RecyclerView.Adapter<AppViewHolder> {
    private final List<AppEntity> mData = new ArrayList<>();

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    public List<AppEntity> getData() {
        return mData;
    }

    public void setData(List<AppEntity> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
