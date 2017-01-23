package com.yueban.installedpackage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IntDef;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.gson.Gson;
import com.yueban.installedpackage.adapter.AppListAdapter;
import com.yueban.installedpackage.entity.AppEntity;
import com.yueban.installedpackage.rx.SimpleSubscriber;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_FILE = 1;
    private RecyclerView mRecyclerView;
    private AppListAdapter mAdapter;
    @ViewType private int mType = ViewType.DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mAdapter = new AppListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        initData(null);
    }

    private void initData(String filePath) {
        final PackageManager packageManager = getPackageManager();
        Observable<List<AppEntity>> applicationListObservable =
            Observable.from(packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES))
                .filter(new Func1<ApplicationInfo, Boolean>() {
                    @Override
                    public Boolean call(ApplicationInfo info) {
                        return (info.flags & ApplicationInfo.FLAG_SYSTEM) <= 0
                            || (info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
                            || (info.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0;
                    }
                })
                .map(new Func1<ApplicationInfo, AppEntity>() {
                    @Override
                    public AppEntity call(ApplicationInfo info) {
                        return new AppEntity(info.loadLabel(packageManager).toString(), info.packageName,
                            info.loadIcon(packageManager), true);
                    }
                })
                .toList();

        switch (mType) {
            case ViewType.DEFAULT:
                applicationListObservable.subscribe(new SimpleSubscriber<List<AppEntity>>() {
                    @Override
                    public void onNext(List<AppEntity> entities) {
                        mAdapter.setData(entities);
                    }
                });
                break;

            case ViewType.IMPORT:
                if (TextUtils.isEmpty(filePath)) {
                    return;
                }
                Observable.just(filePath).map(new Func1<String, List<AppEntity>>() {
                    @Override
                    public List<AppEntity> call(String s) {
                        return new Gson().fromJson(s, new ParameterizedType() {
                            @Override
                            public Type[] getActualTypeArguments() {
                                return new Type[] { AppEntity.class };
                            }

                            @Override
                            public Type getOwnerType() {
                                return null;
                            }

                            @Override
                            public Type getRawType() {
                                return List.class;
                            }
                        });
                    }
                }).zipWith(applicationListObservable, new Func2<List<AppEntity>, List<AppEntity>, List<AppEntity>>() {
                    @Override
                    public List<AppEntity> call(List<AppEntity> entities, List<AppEntity> entities2) {
                        for (AppEntity entity : entities) {
                            entity.isInstalled = !entities.contains(entity);
                        }
                        return entities;
                    }
                }).subscribe(new SimpleSubscriber<List<AppEntity>>() {
                    @Override
                    public void onNext(List<AppEntity> entities) {
                        mAdapter.setData(entities);
                    }
                });
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuExport = menu.findItem(R.id.menu_export);
        menuExport.setVisible(mType == ViewType.DEFAULT);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_import:
                //importAppList();
                Toast.makeText(this, "等待 Wi-Fi，或者设置关闭 “知识文件仅在 Wi-Fi 下上传/下载”", Toast.LENGTH_LONG).show();
                break;

            case R.id.menu_export:
                exportAppList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void importAppList() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    @SuppressLint("SimpleDateFormat")
    private void exportAppList() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String filePath = Environment.getExternalStorageDirectory() + File.separator + "_AppList_" + new SimpleDateFormat(
                    "yyyy-MM-dd_HH:mm:ss").format(new Date()) + ".json";
                try {
                    String json = new Gson().toJson(mAdapter.getData());
                    FileWriter fileWriter = new FileWriter(filePath);
                    fileWriter.write(json);
                    fileWriter.flush();
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(filePath);
                        subscriber.onCompleted();
                    }
                } catch (IOException e) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SimpleSubscriber<String>() {
            @Override
            public void onNext(String s) {
                Snackbar.make(mRecyclerView, getString(R.string.app_list_save_to_format, s), BaseTransientBottomBar.LENGTH_LONG)
                    .show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(mRecyclerView, R.string.export_failed, BaseTransientBottomBar.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_FILE:
                switch (resultCode) {
                    case RESULT_CANCELED:
                        Snackbar.make(mRecyclerView, R.string.cancel_select, BaseTransientBottomBar.LENGTH_LONG).show();
                        break;

                    case RESULT_OK:
                        Uri uri = data.getData();
                        String path;
                        try {
                            path = getPath(this, uri);
                            switchToImportType(path);
                        } catch (URISyntaxException e) {
                            Snackbar.make(mRecyclerView, R.string.load_file_failed, BaseTransientBottomBar.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        break;
                }
                break;
        }
    }

    private void switchToImportType(String path) {
        mType = ViewType.IMPORT;
        invalidateOptionsMenu();
        initData(path);
    }

    private String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor == null) {
                    throw new Exception("cursor init failed");
                }
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it  Or Log it.
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ ViewType.DEFAULT, ViewType.IMPORT })
    public @interface ViewType {
        /**
         * 默认
         */
        int DEFAULT = 0;
        /**
         * 导入模式
         */
        int IMPORT = 1;
    }
}
