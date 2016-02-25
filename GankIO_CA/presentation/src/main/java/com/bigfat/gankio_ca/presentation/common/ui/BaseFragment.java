package com.bigfat.gankio_ca.presentation.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.bigfat.gankio_ca.presentation.common.di.HasComponent;

/**
 * Created by yueban on 16:04 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}
