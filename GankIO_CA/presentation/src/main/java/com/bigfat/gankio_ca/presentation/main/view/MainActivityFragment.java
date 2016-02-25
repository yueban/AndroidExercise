package com.bigfat.gankio_ca.presentation.main.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bigfat.gankio_ca.domain.entity.GankEntity;
import com.bigfat.gankio_ca.presentation.R;
import com.bigfat.gankio_ca.presentation.common.ui.BaseFragment;
import com.bigfat.gankio_ca.presentation.main.adapter.GankListAdapter;
import com.bigfat.gankio_ca.presentation.main.di.GankMainComponent;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment implements MainView {

    @Inject
    MainPresenter mPresenter;
    @Bind(R.id.rv_gank)
    RecyclerView mRvGank;

    private Listener mListener;
    private GankListAdapter mAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, true);
        ButterKnife.bind(this, view);

        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new GankListAdapter(getActivity(), new ArrayList<GankEntity>());
        mRvGank.setLayoutManager(layoutManager);
        mRvGank.setAdapter(mAdapter);
    }

    private void initialize() {
        getComponent(GankMainComponent.class).inject(this);
        mPresenter.setView(this);
        mPresenter.initialize();
    }

    @Override
    public void renderDataList(Collection<GankEntity> gankEntityCollection) {
        mAdapter.setData(gankEntityCollection);
    }

    @Override
    public void viewGankEntity(GankEntity gankEntity) {
        if (mListener != null) {
            mListener.onGankEntityClick(gankEntity);
        }
    }

    @Override
    public void showLoading() {
        this.getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void hideLoading() {
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface Listener {
        void onGankEntityClick(final GankEntity gankEntity);
    }
}
