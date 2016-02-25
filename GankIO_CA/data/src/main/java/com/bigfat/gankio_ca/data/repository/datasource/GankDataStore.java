package com.bigfat.gankio_ca.data.repository.datasource;

import com.bigfat.gankio_ca.domain.entity.DataEntity;
import com.bigfat.gankio_ca.domain.entity.DayEntity;
import rx.Observable;

/**
 * 数据源提供接口
 *
 * Created by yueban on 13:53 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface GankDataStore {
    Observable<DataEntity> dataEntity(String type, int pageSize, int pageIndex);

    Observable<DayEntity> dayEntity(String date);
}
