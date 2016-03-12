package com.bigfat.gankio_ca.presentation.common.rxbus.event;

import com.bigfat.gankio_ca.data.entity.GankEntity;

/**
 * 主界面列表中某一天被点击
 *
 * Created by yueban on 17:50 6/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class DayClickEvent extends Object {
    private GankEntity mGankEntity;

    public GankEntity getGankEntity() {
        return mGankEntity;
    }

    public void setGankEntity(GankEntity gankEntity) {
        mGankEntity = gankEntity;
    }
}
