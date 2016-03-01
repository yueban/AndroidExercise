package com.bigfat.gankio_ca.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.bigfat.gankio_ca.data.entity.DataEntity;
import com.bigfat.gankio_ca.data.entity.DayEntity;
import com.bigfat.gankio_ca.data.entity.GankEntity;
import com.bigfat.gankio_ca.data.exception.APIException;
import com.bigfat.gankio_ca.data.exception.NetworkConnectionException;
import com.bigfat.gankio_ca.data.net.di.DaggerRetrofitComponent;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * API 实现类
 *
 * Created by yueban on 09:59 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class GankApiImpl {
    private Context mContext;
    private GankApi service;

    @Inject
    public GankApiImpl(Context context) {
        this.mContext = context;
        service = DaggerRetrofitComponent.create().retrofit().create(GankApi.class);
    }

    @RxLogObservable
    public Observable<List<GankEntity>> data(final String type, final int pageSize, final int pageIndex) {
        return Observable.create(new Observable.OnSubscribe<List<GankEntity>>() {
            @Override
            public void call(Subscriber<? super List<GankEntity>> subscriber) {
                if (GankApiImpl.this.isThereInternetConnection()) {
                    service
                        .data(type, pageSize, pageIndex)
                        .flatMap(new Func1<DataEntity, Observable<List<GankEntity>>>() {
                            @Override
                            public Observable<List<GankEntity>> call(DataEntity dataEntity) {
                                if (dataEntity.isError()) {
                                    return Observable.error(new APIException());
                                } else {
                                    return Observable.just(dataEntity.getResults());
                                }
                            }
                        }).subscribe(subscriber);
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @RxLogObservable
    public Observable<DayEntity> day(final String date) {
        return Observable.create(new Observable.OnSubscribe<DayEntity>() {
            @Override
            public void call(Subscriber<? super DayEntity> subscriber) {
                if (GankApiImpl.this.isThereInternetConnection()) {
                    service
                        .day(date)
                        .flatMap(new Func1<DayEntity, Observable<DayEntity>>() {
                            @Override
                            public Observable<DayEntity> call(DayEntity dayEntity) {
                                if (dayEntity.isError()) {
                                    return Observable.error(new APIException());
                                } else {
                                    return Observable.just(dayEntity);
                                }
                            }
                        })
                        .subscribe(subscriber);
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    private boolean isThereInternetConnection() {
        ConnectivityManager connectivityManager =
            (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
