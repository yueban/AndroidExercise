package com.bigfat.gankio_ca.presentation.ui.meizhi.presenter;

import com.bigfat.gankio_ca.data.entity.DayEntity;
import com.bigfat.gankio_ca.data.entity.GankEntity;
import com.bigfat.gankio_ca.domain.exception.DefaultErrorBundle;
import com.bigfat.gankio_ca.domain.exception.ErrorBundle;
import com.bigfat.gankio_ca.domain.interactor.DefaultSubscriber;
import com.bigfat.gankio_ca.domain.interactor.GankUseCase;
import com.bigfat.gankio_ca.presentation.common.di.PerActivity;
import com.bigfat.gankio_ca.presentation.common.exception.ErrorMessageFactory;
import com.bigfat.gankio_ca.presentation.common.ui.Presenter;
import com.bigfat.gankio_ca.presentation.ui.meizhi.view.MeizhiView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;

/**
 * Created by yueban on 17:25 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@PerActivity
public class MeizhiPresenter implements Presenter {
    private final GankUseCase mGankUseCase;
    private MeizhiView mView;
    private int mPageIndex = 1;
    private String mType = "福利";
    private boolean isRefreshing = false;

    @Inject
    public MeizhiPresenter(GankUseCase gankUseCase) {
        mGankUseCase = gankUseCase;
    }

    public void setView(MeizhiView view) {
        mView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        mGankUseCase.unSubscribe();
    }

    public void initialize() {
        showViewLoading();
        getData(true);
    }

    public void loadMoreData() {
        mView.loadMoreData();
        getData(false);
    }

    private void getData(boolean isRefresh) {
        if (isRefreshing) {
            return;
        }
        if (isRefresh) {
            mPageIndex = 1;
        } else {
            mPageIndex++;
        }
        mGankUseCase.data(mType, mPageIndex, new DataSubscriber(isRefresh));
    }

    public void onGankEntityClicked(GankEntity gankEntity) {
        try {
            String date = new SimpleDateFormat("yyyy/MM/dd").format(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(gankEntity.getPublishedAt()));
            mGankUseCase.day(date, new Subscriber<DayEntity>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(DayEntity dayEntity) {
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.mView.viewGankEntity(gankEntity);
    }

    private void showViewLoading() {
        mView.showLoading();
    }

    private void hideViewLoading() {
        mView.hideLoading();
    }

    private void showViewRetry() {
        mView.showRetry();
    }

    private void hideViewRetry() {
        mView.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(mView.getContext(),
            errorBundle.getException());
        mView.showError(errorMessage);
    }

    private void showDataInView(List<GankEntity> gankEntities, boolean isRefresh) {
        mView.renderDataList(gankEntities, isRefresh);
    }

    private final class DataSubscriber extends DefaultSubscriber<List<GankEntity>> {
        private boolean isRefresh;

        public DataSubscriber(boolean isRefresh) {
            this.isRefresh = isRefresh;
        }

        @Override
        public void onStart() {
            super.onStart();
            isRefreshing = true;
        }

        @Override
        public void onCompleted() {
            MeizhiPresenter.this.hideViewLoading();
            isRefreshing = false;
        }

        @Override
        public void onError(Throwable e) {
            MeizhiPresenter.this.hideViewLoading();
            MeizhiPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            isRefreshing = false;
        }

        @Override
        public void onNext(List<GankEntity> gankEntities) {
            MeizhiPresenter.this.showDataInView(gankEntities, isRefresh);
        }
    }
}
