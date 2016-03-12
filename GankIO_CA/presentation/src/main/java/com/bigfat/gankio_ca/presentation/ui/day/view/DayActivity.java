package com.bigfat.gankio_ca.presentation.ui.day.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bigfat.gankio_ca.data.entity.GankEntity;
import com.bigfat.gankio_ca.presentation.R;
import com.bigfat.gankio_ca.presentation.common.di.components.DaggerUseCaseComponent;
import com.bigfat.gankio_ca.presentation.common.di.components.UseCaseComponent;
import com.bigfat.gankio_ca.presentation.common.ui.BaseActivity;
import com.bigfat.gankio_ca.presentation.ui.day.presenter.DayPresenter;
import javax.inject.Inject;
import org.parceler.Parcels;

/**
 * Created by yueban on 23:52 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class DayActivity extends BaseActivity implements DayView {
    private static final String INTENT_EXTRA_PARAM_GANKENTITY = "org.android10.INTENT_PARAM_GANKENTITY";

    @Inject DayPresenter mPresenter;

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.rv_gank) RecyclerView mRvGank;
    @Bind(R.id.srl_gank) SwipeRefreshLayout mSrlGank;

    public static Intent getCallingIntent(Context context, GankEntity gankEntity) {
        Intent intent = new Intent(context, DayActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_GANKENTITY, Parcels.wrap(gankEntity));
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(DayActivity.class.getSimpleName());
        initializeInjector();
        initialize();
    }

    private void initializeInjector() {
        UseCaseComponent useCaseComponent = DaggerUseCaseComponent.builder()
            .applicationComponent(getApplicationComponent())
            .activityModule(getActivityModule())
            .build();
        useCaseComponent.inject(this);
    }

    private void initialize() {
        mPresenter.setView(this);
        mPresenter.initialize();
        GankEntity gankEntity = Parcels.unwrap(getIntent().getParcelableExtra(INTENT_EXTRA_PARAM_GANKENTITY));
        mPresenter.initData(gankEntity);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context getContext() {
        return getApplicationComponent().context();
    }
}
