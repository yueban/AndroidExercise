package com.yueban.installedpackage.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.yueban.installedpackage.R;
import com.yueban.installedpackage.entity.AppEntity;

/**
 * @author yueban
 * @date 2017/1/17
 * @email fbzhh007@gmail.com
 */
public class AppViewHolder extends RecyclerView.ViewHolder {
    private ImageView mIvStatus;
    private TextView mTvName;

    public AppViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false));

        mIvStatus = (ImageView) itemView.findViewById(R.id.iv_status);
        mTvName = (TextView) itemView.findViewById(R.id.tv_name);
    }

    public void bind(AppEntity entity) {
        mIvStatus.setVisibility(entity.isInstalled ? View.VISIBLE : View.INVISIBLE);
        mIvStatus.setImageDrawable(entity.icon);
        mTvName.setText(entity.appName + " (" + entity.appPackageName + ")");
    }
}
