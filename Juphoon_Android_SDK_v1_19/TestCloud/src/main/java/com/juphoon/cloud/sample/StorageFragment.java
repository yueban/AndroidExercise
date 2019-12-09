package com.juphoon.cloud.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.juphoon.cloud.JCStorage;
import com.juphoon.cloud.JCStorageItem;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCEvent;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCStorageEvent;
import com.juphoon.cloud.sample.JCWrapper.JCManager;
import com.juphoon.cloud.sample.Toos.FileUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class StorageFragment extends Fragment implements View.OnClickListener {

    private EditText mTxtFileUri;
    private TextView mTxtFileLog;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_storage, container, false);
        mTxtFileUri = (EditText) rootView.findViewById(R.id.editTextFileUri);
        rootView.findViewById(R.id.btnUpload).setOnClickListener(this);
        rootView.findViewById(R.id.btnDownLoad).setOnClickListener(this);
        rootView.findViewById(R.id.btnChooseFile).setOnClickListener(this);
        mTxtFileLog = (TextView) rootView.findViewById(R.id.txtFileLog);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(JCEvent event) {
        if (event.getEventType() == JCEvent.EventType.STORAGE) {
            JCStorageEvent jcStorageEvent = (JCStorageEvent) event;
            StringBuffer log = new StringBuffer();
            if (jcStorageEvent.item.getState() == JCStorage.ITEM_STATE_TRANSFERRING) {
                log.append("\n" + getContext().getString(R.string.storage_transferring))
                    .append(jcStorageEvent.item.getProgress()).append("%");
            } else if (jcStorageEvent.item.getState() == JCStorage.ITEM_STATE_OK) {
                if (jcStorageEvent.item.getDirection() == JCStorage.DIRECTION_UPLOAD) {
                    log.append("\n" + getContext().getString(R.string.storage_upload_ok))
                            .append("\n" + getContext().getString(R.string.storage_download_uri))
                            .append(jcStorageEvent.item.getUri());
                    mTxtFileUri.setText(jcStorageEvent.item.getUri());
                } else if(jcStorageEvent.item.getDirection() == JCStorage.DIRECTION_DOWNLOAD) {
                    log.append("\n " + getContext().getString(R.string.storage_download_ok))
                        .append(jcStorageEvent.item.getPath());
                }
            } else if (jcStorageEvent.item.getState() == JCStorage.ITEM_STATE_FAIL) {
                log.append("\n" + getContext().getString(R.string.storage_transfer_failed)) ;
            }
            mTxtFileLog.setText(log);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                String path;
                if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                    path = uri.getPath();
                    mTxtFileUri.setText(path);
                    return;
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                    path = FileUtils.getPath(getActivity(), uri);
                } else {//4.4以下下系统调用方法
                    path = FileUtils.getRealPathFromURI(getActivity(), uri);
                }
                mTxtFileUri.setText(path);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnUpload:
                onUpload(v);
                break;
            case R.id.btnDownLoad:
                onDownload(v);
                break;
            case R.id.btnChooseFile:
                onChooseFile(v);
                break;
            default:
                break;
        }
    }

    public void onUpload(View view) {
        mTxtFileUri.setError(null);
        String fileUri = mTxtFileUri.getText().toString();
        if (!TextUtils.isEmpty(fileUri)) {
            JCStorageItem jcStorageItem = JCManager.getInstance().storage.uploadFile(fileUri);
            if (jcStorageItem == null || (jcStorageItem != null && jcStorageItem.getReason() != JCStorage.REASON_NONE)) {
                mTxtFileUri.setError(getString(R.string.error_field_required));
            }
        } else {
            mTxtFileUri.setError(getString(R.string.error_field_required));
        }
    }

    public void onDownload(View view) {
        mTxtFileUri.setError(null);
        String fileUri = mTxtFileUri.getText().toString();
        if (!TextUtils.isEmpty(fileUri)) {
            JCStorageItem jcStorageItem = JCManager.getInstance().storage.downloadFile(fileUri, Environment.getExternalStorageDirectory()  + "/test.dat");
            if (jcStorageItem == null || (jcStorageItem != null && jcStorageItem.getReason() != JCStorage.REASON_NONE)) {
                mTxtFileUri.setError(getString(R.string.error_field_required));
            }
        } else {
            mTxtFileUri.setError(getString(R.string.error_field_required));
        }
    }

    public void onChooseFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }

}
