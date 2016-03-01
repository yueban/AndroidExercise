package com.bigfat.gankio_ca.data.datasource;

import com.bigfat.gankio_ca.data.entity.DayEntity;
import com.bigfat.gankio_ca.data.entity.GankEntity;
import java.util.List;
import rx.Observable;

/**
 * 数据源提供接口
 *
 * Created by yueban on 13:53 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface GankDataStore {
    Observable<List<GankEntity>> data(String type, int pageSize, int pageIndex);

    Observable<DayEntity> day(String date);
}
