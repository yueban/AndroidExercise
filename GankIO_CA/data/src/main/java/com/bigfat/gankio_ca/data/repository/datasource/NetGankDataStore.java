package com.bigfat.gankio_ca.data.repository.datasource;

import com.bigfat.gankio_ca.data.net.GankApi;
import com.bigfat.gankio_ca.data.net.GankApiImpl;
import com.bigfat.gankio_ca.domain.entity.DataEntity;
import com.bigfat.gankio_ca.domain.entity.DayEntity;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

/**
 * Created by yueban on 13:55 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class NetGankDataStore implements GankDataStore {
    private final GankApi mGankApi;

    @Inject
    public NetGankDataStore(GankApiImpl gankApi) {
        mGankApi = gankApi;
    }

    @Override
    public Observable<DataEntity> dataEntity(String type, int pageSize, int pageIndex) {
        return mGankApi.data(type, pageSize, pageIndex);
    }

    @Override
    public Observable<DayEntity> dayEntity(String date) {
        return mGankApi.day(date);
    }
}
