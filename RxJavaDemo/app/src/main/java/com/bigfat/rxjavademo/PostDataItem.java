package com.bigfat.rxjavademo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import kale.adapter.item.AdapterItem;

/**
 * Created by david on 15/12/1.
 */
public class PostDataItem implements AdapterItem<PostData> {

    private TextView mTVName;
    private TextView mTVContent;
    private ImageView mIvAvatar;
    private ImageView mIvImage;



    @Override
    public int getLayoutResId() {
        return R.layout.item_post;
    }

    @Override
    public void bindViews(View view) {
        mTVName = (TextView) view.findViewById(R.id.tv_user_post_name);
        mIvAvatar = (ImageView) view.findViewById(R.id.iv_user_post_avatar);
        mTVContent = (TextView) view.findViewById(R.id.tv_post_content);
        mIvImage = (ImageView) view.findViewById(R.id.iv_user_post_image);
    }

    @Override
    public void setViews() {

    }

    @Override
    public void handleData(PostData postData, int i) {
        mTVName .setText(postData.getUser().getName());
        mTVContent.setText(postData.getText());
        if(postData.getDetails().size() > 0) {
            mIvImage.setVisibility(View.VISIBLE);
            Picasso.with(mIvImage.getContext()).load(postData.getDetails().get(0).getMiddle_pic()).into(mIvImage);
        }else{
            mIvImage.setVisibility(View.GONE);
        }
        Picasso.with(mIvAvatar.getContext()).load(postData.getUser().getAvatar()).into(mIvAvatar);
    }
}
