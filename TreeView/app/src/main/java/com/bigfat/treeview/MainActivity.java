package com.bigfat.treeview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.bigfat.treeview.adpter.SimpleTreeListViewAdapter;
import com.bigfat.treeview.bean.FileBean;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ListView mListView;
    private SimpleTreeListViewAdapter<FileBean> mAdapter;
    private List<FileBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDatas();
        mListView = (ListView) findViewById(R.id.id_listView);
        try {
            mAdapter = new SimpleTreeListViewAdapter<>(MainActivity.this, mDatas, 1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
    }

    private void initDatas() {
        FileBean fileBean = new FileBean(1, 0, "根目录1");
        mDatas.add(fileBean);
        fileBean = new FileBean(2, 0, "根目录2");
        mDatas.add(fileBean);
        fileBean = new FileBean(3, 0, "根目录3");
        mDatas.add(fileBean);
        fileBean = new FileBean(4, 1, "根目录1-1");
        mDatas.add(fileBean);
        fileBean = new FileBean(5, 1, "根目录1-2");
        mDatas.add(fileBean);
        fileBean = new FileBean(6, 5, "根目录1-2-1");
        mDatas.add(fileBean);
        fileBean = new FileBean(7, 3, "根目录3-1");
        mDatas.add(fileBean);
        fileBean = new FileBean(8, 3, "根目录3-2");
        mDatas.add(fileBean);
    }
}
