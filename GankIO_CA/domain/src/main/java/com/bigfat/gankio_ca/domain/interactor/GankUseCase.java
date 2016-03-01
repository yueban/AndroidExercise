package com.bigfat.gankio_ca.domain.interactor;

import com.bigfat.gankio_ca.data.cache.GankCache;
import com.bigfat.gankio_ca.data.datasource.GankDataStore;
import com.bigfat.gankio_ca.data.entity.DayEntity;
import com.bigfat.gankio_ca.data.entity.GankEntity;
import com.bigfat.gankio_ca.domain.executor.PostExecutionTread;
import com.bigfat.gankio_ca.domain.executor.ThreadExecutor;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * 用例:数据列表
 *
 * Created by yueban on 13:18 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class GankUseCase extends UseCase {
    private final static int PAGE_SIZE = 10;
    private final GankDataStore mGankDataStore;
    private final GankCache mGankCache;

    @Inject
    public GankUseCase(ThreadExecutor threadExecutor, PostExecutionTread postExecutionTread,
        GankDataStore gankDataStore, GankCache gankCache) {
        super(threadExecutor, postExecutionTread);
        mGankDataStore = gankDataStore;
        mGankCache = gankCache;
    }

    public void data(String type, int pageIndex, Subscriber<List<GankEntity>> subscriber) {
        Observable<List<GankEntity>> observable = mGankDataStore.data(type, PAGE_SIZE, pageIndex);
        if (pageIndex == 1) {
            observable = observable.doOnNext(
                new Action1<List<GankEntity>>() {
                    @Override
                    public void call(List<GankEntity> gankEntities) {
                        mGankCache.clearData();
                        mGankCache.saveData(gankEntities);
                    }
                });
            observable = Observable.concat(
                mGankCache.getData(type, PAGE_SIZE, pageIndex),
                observable);
        }
        execute(observable, subscriber);
    }

    public void day(String date, Subscriber<DayEntity> subscriber) {
        execute(mGankDataStore.day(date), subscriber);
    }
}
