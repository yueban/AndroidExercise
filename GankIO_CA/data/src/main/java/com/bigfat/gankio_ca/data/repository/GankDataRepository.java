package com.bigfat.gankio_ca.data.repository;

import com.bigfat.gankio_ca.data.repository.datasource.GankDataStoreFactory;
import com.bigfat.gankio_ca.domain.entity.DataEntity;
import com.bigfat.gankio_ca.domain.entity.DayEntity;
import com.bigfat.gankio_ca.domain.repository.GankRepository;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

/**
 * Created by yueban on 13:48 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class GankDataRepository implements GankRepository {
    private final GankDataStoreFactory mGankDataStoreFactory;

    @Inject
    public GankDataRepository(GankDataStoreFactory gankDataStoreFactory) {
        mGankDataStoreFactory = gankDataStoreFactory;
    }

    @Override
    public Observable<DataEntity> data(String type, int pageSize, int pageIndex) {
        return mGankDataStoreFactory.createNetDataStore().dataEntity(type, pageSize, pageIndex);
    }

    @Override
    public Observable<DayEntity> day(String date) {
        return mGankDataStoreFactory.createNetDataStore().dayEntity(date);
    }
}
