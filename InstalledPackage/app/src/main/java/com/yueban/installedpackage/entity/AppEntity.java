package com.yueban.installedpackage.entity;

import android.graphics.drawable.Drawable;

/**
 * @author yueban
 * @date 2017/1/17
 * @email fbzhh007@gmail.com
 */
public class AppEntity {
    public String appName;
    public String appPackageName;
    public transient Drawable icon;
    public boolean isInstalled;

    public AppEntity() {
    }

    public AppEntity(String appName, String appPackageName, Drawable icon, boolean isInstalled) {
        this.appName = appName;
        this.appPackageName = appPackageName;
        this.icon = icon;
        this.isInstalled = isInstalled;
    }
}
