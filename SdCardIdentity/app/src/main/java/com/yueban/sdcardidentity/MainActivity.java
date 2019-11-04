package com.yueban.sdcardidentity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "yueban_sdcard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MainActivityPermissionsDispatcher.getSdCardCid1WithPermissionCheck(this);
        MainActivityPermissionsDispatcher.getSdCardCid2WithPermissionCheck(this);
        MainActivityPermissionsDispatcher.getSdCardCid3WithPermissionCheck(this);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void getSdCardCid1() {
        File extDir = Environment.getExternalStorageDirectory();
        if (extDir == null || !extDir.exists()) {
            Log.e(TAG, "extDir is null");
            return;
        }
        try {
            File input = new File("/sys/class/mmc_host/mmc1");
            String cid_directory = null;
            File[] sid = input.listFiles();

            for (int i = 0; i < sid.length; i++) {
                if (sid[i].toString().contains("mmc1:")) {
                    cid_directory = sid[i].toString();
                    String SID = (String) sid[i].toString().subSequence(cid_directory.length() - 4, cid_directory.length());
                    Log.d(TAG, " SID of MMC = " + SID);
                    break;
                }
            }
            BufferedReader CID = new BufferedReader(new FileReader(cid_directory + "/cid"));
            String sd_cid = CID.readLine();
            Log.d(TAG, "CID of the MMC = " + sd_cid);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void getSdCardCid2() {
        File[] externalStorageFiles = ContextCompat.getExternalFilesDirs(this, null);
        Log.d(TAG, "mount_size: " + externalStorageFiles.length);
        for (File file : externalStorageFiles) {
            if (file == null) {
                continue;
            }
            String path = file.getAbsolutePath();
            path = path.replaceAll("/Android/data/" + getPackageName() + "/files", "");
            Log.d(TAG, path);
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void getSdCardCid3() {
        List<StorageUtils.StorageInfo> list = StorageUtils.getStorageList();
        for (StorageUtils.StorageInfo info : list) {
            Log.d(TAG, info.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
