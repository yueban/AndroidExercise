package com.juphoon.cloud.sample;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.juphoon.cloud.sample.JCWrapper.JCManager;
import com.juphoon.cloud.sample.Toos.Utils;
import com.tencent.bugly.crashreport.CrashReport;

public class JCApplication extends Application {

    private int mFrontActivityCount;
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        String processName = Utils.getCurProcessName(this);
        if (TextUtils.equals(processName, getPackageName())) {
            JCManager.getInstance().initialize(this);
            setupCheckForeground();
        }
        sContext = this;
        initializeBugly();
    }

    private void setupCheckForeground() {
        mFrontActivityCount = 0;
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (mFrontActivityCount == 0) {
                    JCManager.getInstance().client.setForeground(true);
                }
                mFrontActivityCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mFrontActivityCount--;
                if (mFrontActivityCount == 0) {
                    JCManager.getInstance().client.setForeground(false);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void initializeBugly() {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setAppVersion(Utils.getAppVersionName(this) + "." + Utils.getAppVersionCode(this));
        CrashReport.initCrashReport(this, "d0b1e2ba40", false, strategy);
    }
}
