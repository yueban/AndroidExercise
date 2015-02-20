package com.bigfat.splashscreen.wxapi;


import android.app.Activity;
import android.os.Bundle;

import com.bigfat.splashscreen.MainActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
//        String result;
//        switch (baseResp.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
//                result = getResources().getString(R.string.errcode_success);
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                result = getResources().getString(R.string.errcode_cancel);
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                result = getResources().getString(R.string.errcode_deny);
//                break;
//            default:
//                result = getResources().getString(R.string.errcode_unknown);
//                break;
//        }
//        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
    }
}