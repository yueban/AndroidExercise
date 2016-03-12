package com.bigfat.gankio_ca.presentation.ui.meizhi.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bigfat.gankio_ca.data.entity.GankEntity;
import com.bigfat.gankio_ca.presentation.R;
import com.bigfat.gankio_ca.presentation.common.di.components.UseCaseComponent;
import com.bigfat.gankio_ca.presentation.common.listeners.OnItemClickListener;
import com.bigfat.gankio_ca.presentation.common.ui.BaseFragment;
import com.bigfat.gankio_ca.presentation.ui.meizhi.presenter.MeizhiPresenter;
import com.bigfat.gankio_ca.presentation.ui.meizhi.view.adapter.MeizhiListAdapter;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MeizhiFragment extends BaseFragment implements MeizhiView {

    @Inject MeizhiPresenter mPresenter;

    @Bind(R.id.srl_gank) SwipeRefreshLayout mSrlGank;
    @Bind(R.id.rv_gank) RecyclerView mRvGank;

    private Listener mListener;
    private MeizhiListAdapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;

    public MeizhiFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, true);
        ButterKnife.bind(this, view);

        initView();
        bindEvent();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initView() {
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mAdapter = new MeizhiListAdapter(getActivity(), new ArrayList<GankEntity>());
        mRvGank.setLayoutManager(mLayoutManager);
        mRvGank.setAdapter(mAdapter);
    }

    private void bindEvent() {
        mSrlGank.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.initialize();
                }
            }
        });
        mRvGank.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mLayoutManager != null) {
                    int[] last = new int[2];
                    mLayoutManager.findLastCompletelyVisibleItemPositions(last);
                    if (last[0] >= mAdapter.getItemCount() - 1
                        || last[1] >= mAdapter.getItemCount() - 1) {
                        mPresenter.loadMoreData();
                    }
                }
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener<GankEntity>() {
            @Override
            public void onClick(View view, GankEntity gankEntity, int position) {
                if (mPresenter != null && gankEntity != null) {
                    mPresenter.onGankEntityClicked(gankEntity);
                }
            }
        });
    }

    private void initialize() {
        getComponent(UseCaseComponent.class).inject(this);
        mPresenter.setView(this);
        mPresenter.initialize();
    }

    @Override
    public void renderDataList(Collection<GankEntity> gankEntityCollection, boolean isRefresh) {
        if (isRefresh) {
            mAdapter.setData(gankEntityCollection);
        } else {
            mAdapter.addData(gankEntityCollection);
        }
    }

    @Override
    public void loadMoreData() {
        showLoading();
    }

    @Override
    public void viewDay(GankEntity gankEntity) {
        if (mListener != null) {
            mListener.onGankEntityClick(gankEntity);
        }
    }

    @Override
    public void showLoading() {
        mSrlGank.post(new Runnable() {
            @Override
            public void run() {
                mSrlGank.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideLoading() {
        mSrlGank.setRefreshing(false);
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
