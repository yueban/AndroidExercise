package com.bigfat.md_app_clean.presentation.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.bigfat.md_app_clean.presentation.common.di.HasActivityComponent;
import com.bigfat.md_app_clean.presentation.common.di.components.ActivityComponent;

/**
 * Created by yueban on 15:50 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    ActivityComponent getActivityComponent() {
        return ((HasActivityComponent) getActivity()).getActivityComponent();
    }
}
