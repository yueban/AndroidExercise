package com.bigfat.md_app_clean.presentation.ui.login.presenter;

import com.bigfat.md_app_clean.data.entity.dbentity.LoginAccountEntity;
import com.bigfat.md_app_clean.data.entity.viewentity.AuthEntity;
import com.bigfat.md_app_clean.domain.di.PerActivity;
import com.bigfat.md_app_clean.domain.interactor.LoginUseCase;
import com.bigfat.md_app_clean.presentation.common.exception.DefaultErrorBundle;
import com.bigfat.md_app_clean.presentation.common.exception.ErrorBundle;
import com.bigfat.md_app_clean.presentation.common.exception.ErrorMessageFactory;
import com.bigfat.md_app_clean.presentation.ui.login.view.LoginView;
import javax.inject.Inject;
import rx.Subscriber;

/**
 * Created by yueban on 16:46 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@PerActivity
public class LoginPresenterImpl implements LoginPresenter {
    private final LoginUseCase mLoginUseCase;
    private LoginView mView;

    @Inject
    public LoginPresenterImpl(LoginUseCase loginUseCase) {
        mLoginUseCase = loginUseCase;
    }

    @Override
    public void login(String account, String password) {
        mView.showLoading();
        mLoginUseCase.login(account, password, new LoginSubscriber());
    }

    @Override
    public void setView(LoginView loginView) {
        mView = loginView;

        initialize();
    }

    private void initialize() {
        mLoginUseCase.getLastLoginAccount().subscribe(new Subscriber<LoginAccountEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LoginAccountEntity loginAccountEntity) {
                mView.setAccount(loginAccountEntity.getAccount());
                //mView.setAccount(loginAccountEntity.getPassword());
                mView.showToast(loginAccountEntity.toString());
                login(loginAccountEntity.getAccount(), loginAccountEntity.getPassword());
            }
        });
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        mLoginUseCase.unSubscribe();
    }

    @Override
    public void showErrorMessage(ErrorBundle errorBundle) {
        mView.showToast(ErrorMessageFactory.create(mView.getContext(), errorBundle.getException()));
    }

    class LoginSubscriber extends Subscriber<AuthEntity> {
        @Override
        public void onCompleted() {
            mView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            mView.hideLoading();
            showErrorMessage(new DefaultErrorBundle(e));
        }

        @Override
        public void onNext(AuthEntity authEntity) {
            mView.loginSuccess(authEntity);
        }
    }
}
