package com.bigfat.gankio_ca.presentation.main.view;

import com.bigfat.gankio_ca.domain.entity.DataEntity;
import com.bigfat.gankio_ca.domain.exception.DefaultErrorBundle;
import com.bigfat.gankio_ca.domain.exception.ErrorBundle;
import com.bigfat.gankio_ca.domain.interactor.DefaultSubscriber;
import com.bigfat.gankio_ca.domain.interactor.UseCase;
import com.bigfat.gankio_ca.presentation.common.ui.Presenter;
import com.bigfat.gankio_ca.presentation.common.di.PerActivity;
import com.bigfat.gankio_ca.presentation.common.exception.ErrorMessageFactory;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by yueban on 17:25 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@PerActivity
public class MainPresenter implements Presenter {
    private final UseCase mGetDataUseCase;
    private MainView mView;

    @Inject
    public MainPresenter(@Named("GetDataUseCase") UseCase getDataUseCase) {
        mGetDataUseCase = getDataUseCase;
    }

    public void setView(MainView view) {
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
        mGetDataUseCase.unSubscribe();
    }

    public void initialize() {
        mGetDataUseCase.excute(new DataSubscriber());
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

    private void showDataInView(DataEntity dataEntity) {
        mView.renderDataList(dataEntity.getResults());
    }

    private final class DataSubscriber extends DefaultSubscriber<DataEntity> {
        @Override
        public void onCompleted() {
            MainPresenter.this.hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            MainPresenter.this.hideViewLoading();
            MainPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
        }

        @Override
        public void onNext(DataEntity dataEntity) {
            super.onNext(dataEntity);
            MainPresenter.this.showDataInView(dataEntity);
        }
    }
}
