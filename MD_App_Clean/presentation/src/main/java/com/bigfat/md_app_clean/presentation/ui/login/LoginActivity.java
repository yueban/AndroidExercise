package com.bigfat.md_app_clean.presentation.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bigfat.md_app_clean.data.entity.viewentity.AuthEntity;
import com.bigfat.md_app_clean.presentation.R;
import com.bigfat.md_app_clean.presentation.common.ui.BaseActivity;
import com.bigfat.md_app_clean.presentation.ui.login.presenter.LoginPresenterImpl;
import com.bigfat.md_app_clean.presentation.ui.login.view.LoginView;
import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements LoginView {
    @Inject LoginPresenterImpl mPresenter;

    @Bind(R.id.btn_login) Button mBtnLogin;
    @Bind(R.id.et_password) EditText mEtPassword;
    @Bind(R.id.et_account) EditText mEtAccount;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initialize();
    }

    private void initialize() {
        mActivityComponent.inject(this);
        mPresenter.setView(this);
        mEtAccount.requestFocus();
    }

    @OnClick(R.id.btn_login)
    @Override
    public void onLoginButtonClick() {
        mPresenter.login(mEtAccount.getText().toString(), mEtPassword.getText().toString());
    }

    @Override
    public void loginSuccess(AuthEntity authEntity) {
        getUtilComponent().toastUtil().show(authEntity.toString());
    }

    @Override
    public void setAccount(String account) {
        mEtAccount.setText("");
        mEtAccount.append(account);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String message) {
        getUtilComponent().toastUtil().show(message);
    }

    public Context getContext() {
        return getActivityComponent().activity();
    }
}
