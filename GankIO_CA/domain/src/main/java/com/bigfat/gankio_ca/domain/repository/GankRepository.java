package com.bigfat.gankio_ca.domain.repository;

import com.bigfat.gankio_ca.domain.entity.DataEntity;
import com.bigfat.gankio_ca.domain.entity.DayEntity;
import rx.Observable;

/**
 * 提供gankIO相关数据的接口
 *
 * Created by yueban on 13:03 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface GankRepository {
    Observable<DataEntity> data(String type, int pageSize, int pageIndex);

    Observable<DayEntity> day(String date);
}