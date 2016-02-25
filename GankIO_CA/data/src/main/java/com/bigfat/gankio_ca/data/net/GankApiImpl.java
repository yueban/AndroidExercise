package com.bigfat.gankio_ca.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.bigfat.gankio_ca.data.exception.NetworkConnectionException;
import com.bigfat.gankio_ca.data.net.di.DaggerRetrofitComponent;
import com.bigfat.gankio_ca.domain.entity.DataEntity;
import com.bigfat.gankio_ca.domain.entity.DayEntity;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.Subscriber;

/**
 * API 实现类
 *
 * Created by yueban on 09:59 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class GankApiImpl implements GankApi {
    private Context mContext;
    private GankApi service;

    @Inject
    public GankApiImpl(Context context) {
        this.mContext = context;
        service = DaggerRetrofitComponent.create().getRetrofit().create(GankApi.class);
    }

    @RxLogObservable
    @Override
    public Observable<DataEntity> data(final String type, final int pageSize, final int pageIndex) {
        return Observable.create(new Observable.OnSubscribe<DataEntity>() {
            @Override
            public void call(Subscriber<? super DataEntity> subscriber) {
                if (GankApiImpl.this.isThereInternetConnection()) {
                    service.data(type, pageSize, pageIndex).subscribe(subscriber);
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @RxLogObservable
    @Override
    public Observable<DayEntity> day(final String date) {
        return Observable.create(new Observable.OnSubscribe<DayEntity>() {
            @Override
            public void call(Subscriber<? super DayEntity> subscriber) {
                if (GankApiImpl.this.isThereInternetConnection()) {
                    service.day(date).subscribe(subscriber);
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
