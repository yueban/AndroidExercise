package com.bigfat.gankio_ca.presentation.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.bigfat.gankio_ca.presentation.common.App;
import com.bigfat.gankio_ca.presentation.common.di.components.ApplicationComponent;
import com.bigfat.gankio_ca.presentation.common.di.modules.ActivityModule;
import com.bigfat.gankio_ca.presentation.common.navigator.Navigator;
import com.bigfat.gankio_ca.presentation.common.rxbus.RxBus;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by yueban on 15:43 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class BaseActivity extends AppCompatActivity {
    private Navigator mNavigator;
    private RxBus mRxBus;
    //RxBus订阅
    private Subscription mSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getApplicationComponent().inject(this);
        mNavigator = getApplicationComponent().navigator();
        mRxBus = getApplicationComponent().rxBus();

        mSubscription = mRxBus.toObservable().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                onEvent(o);
            }
        });
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((App) getApplication()).getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    protected void sendEvent(Object o) {
        mRxBus.send(o);
    }

    protected void onEvent(Object o) {

    }

    public Navigator getNavigator() {
        return mNavigator;
    }

    public RxBus getRxBus() {
        return mRxBus;
    }
}
