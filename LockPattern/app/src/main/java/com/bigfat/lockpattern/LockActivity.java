package com.bigfat.lockpattern;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;

import com.bigfat.lockpattern.view.LockPatternView;


public class LockActivity extends ActionBarActivity {

    private static final String TAG = "LockActivity";

    private LockPatternView mLockPattern;
    private String mPassword;
    /**
     * 密码是否有效
     */
    private boolean isPasswordEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        mPassword = sp.getString("password", "");
        Log.i(TAG, "mPassword--->" + mPassword);
        isPasswordEnable = !TextUtils.isEmpty(mPassword) && mPassword.length() >= 5;

        mLockPattern = (LockPatternView) findViewById(R.id.id_lock_pattern);

        mLockPattern.setmOnPatternChangeListener(new LockPatternView.OnPatternChangeListener() {
            @Override
            public void onPatternChange(String password) {
                if (!TextUtils.isEmpty(password)) {
                    if (isPasswordEnable) {
                        if (password.equals(mPassword)) {
                            goToMainActivity();
                        }
                    } else {
                        getPreferences(MODE_PRIVATE).edit().putString("password", password).apply();
                        goToMainActivity();
                    }
                }
            }
        });
    }

    /**
     * 跳转到MainActivity
     */
    private void goToMainActivity() {
        Intent intent = new Intent(LockActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
