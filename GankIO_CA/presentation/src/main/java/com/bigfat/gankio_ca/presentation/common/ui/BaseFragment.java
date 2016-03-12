package com.bigfat.gankio_ca.presentation.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.bigfat.gankio_ca.presentation.common.di.HasComponent;
import com.bigfat.gankio_ca.presentation.common.rxbus.RxBus;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by yueban on 16:04 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class BaseFragment extends Fragment {
    private Subscription mSubscription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mSubscription = getRxBus().toObservable().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                onEvent(o);
            }
        });
    }

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    protected RxBus getRxBus() {
        return ((BaseActivity) getActivity()).getRxBus();
    }

    protected void sendEvent(Object o) {
        getRxBus().send(o);
    }

    protected void onEvent(Object o) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
