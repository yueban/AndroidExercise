package com.bigfat.gankio_ca.data.datasource;

import com.bigfat.gankio_ca.data.entity.DayEntity;
import com.bigfat.gankio_ca.data.entity.GankEntity;
import com.bigfat.gankio_ca.data.net.GankApiImpl;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

/**
 * Created by yueban on 13:55 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class GankDataStoreNet implements GankDataStore {
    private final GankApiImpl mGankApi;

    @Inject
    public GankDataStoreNet(GankApiImpl GankApi) {
        mGankApi = GankApi;
    }

    @Override
    public Observable<List<GankEntity>> data(String type, int pageSize, int pageIndex) {
        return mGankApi
            .data(type, pageSize, pageIndex);
    }

    @Override
    public Observable<DayEntity> day(String date) {
        return mGankApi.day(date);
    }
}
