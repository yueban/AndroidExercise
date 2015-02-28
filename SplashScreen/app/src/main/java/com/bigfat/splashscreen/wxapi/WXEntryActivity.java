package com.bigfat.splashscreen.wxapi;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.bigfat.splashscreen.MainActivity;
import com.bigfat.splashscreen.Util;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.social.UMPlatformData;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        UMPlatformData platform = new UMPlatformData(UMPlatformData.UMedia.WEIXIN_FRIENDS, Util.getDeviceInfo(this));
        MobclickAgent.onSocialEvent(this, platform);

        Log.i(TAG, Util.getDeviceInfo(this));

        finish();
    }
}