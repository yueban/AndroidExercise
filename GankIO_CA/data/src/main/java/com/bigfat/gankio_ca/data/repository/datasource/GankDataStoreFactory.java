package com.bigfat.gankio_ca.data.repository.datasource;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Gank数据源工厂
 *
 * Created by yueban on 14:07 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class GankDataStoreFactory {
    private final NetGankDataStore mNetGankDataStore;

    @Inject
    public GankDataStoreFactory(NetGankDataStore netGankDataStore) {
        mNetGankDataStore = netGankDataStore;
    }

    public GankDataStore createNetDataStore() {
        return mNetGankDataStore;
    }
}
