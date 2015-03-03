package com.bigfat.lockpattern;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.bigfat.lockpattern.view.LockPatternView;


public class LockActivity extends ActionBarActivity {

    private LockPatternView mLockPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        mLockPattern = (LockPatternView) findViewById(R.id.id_lock_pattern);

        mLockPattern.setmOnPatternChangeListener(new LockPatternView.OnPatternChangeListener() {
            @Override
            public void onPatternChange(String password) {
                if (!TextUtils.isEmpty(password)) {
                    Toast.makeText(LockActivity.this, password, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
