package com.yueban.aidlclientdemo;

import com.codezjx.andlinker.annotation.RemoteInterface;

@RemoteInterface
public interface IRemoteService {
    int getPid();

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString);

//    void registerCallback(@Callback IRemoteCallback callback);

//    void directionalParamMethod(@In int[] arr, @Out ParcelableObj obj, @Inout Rect rect);

//    @OneWay
//    void onewayMethod(String msg);
}