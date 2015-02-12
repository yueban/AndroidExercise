package com.bigfat.treeview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bigfat.treeview.adpter.SimpleTreeListViewAdapter;
import com.bigfat.treeview.bean.FileBean;
import com.bigfat.treeview.bean.OrgBean;
import com.bigfat.treeview.utils.Node;
import com.bigfat.treeview.utils.listener.OnTreeListViewItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ListView mListView;
    private SimpleTreeListViewAdapter<OrgBean> mAdapter;
    private List<FileBean> mDatas;
    private List<OrgBean> mDatas2;
    /**
     * 默认展开层级
     */
    public static final int DEFAULT_EXPAND_LEVEL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDatas();
        mListView = (ListView) findViewById(R.id.id_listView);
        try {
            mAdapter = new SimpleTreeListViewAdapter<>(MainActivity.this, mDatas2, DEFAULT_EXPAND_LEVEL);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
        mAdapter.setOnTreeListViewItemClickListener(new OnTreeListViewItemClickListener() {
            @Override
            public void onClick(Node node, int position) {
                if (node.isLeaf()) {
                    Toast.makeText(MainActivity.this, node.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this).setTitle("Add Node").setView(editText).setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            Toast.makeText(MainActivity.this, "添加项不能为空", Toast.LENGTH_SHORT).show();
                        }
                        mAdapter.addExtraNode(position, editText.getText().toString());
                    }
                }).setNegativeButton("Cancel", null).show();
                return true;
            }
        });
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
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

        mDatas2 = new ArrayList<>();
        OrgBean orgBean = new OrgBean(1, 0, "根目录1");
        mDatas2.add(orgBean);
        orgBean = new OrgBean(2, 0, "根目录2");
        mDatas2.add(orgBean);
        orgBean = new OrgBean(3, 0, "根目录3");
        mDatas2.add(orgBean);
        orgBean = new OrgBean(4, 1, "根目录1-1");
        mDatas2.add(orgBean);
        orgBean = new OrgBean(5, 1, "根目录1-2");
        mDatas2.add(orgBean);
        orgBean = new OrgBean(6, 5, "根目录1-2-1");
        mDatas2.add(orgBean);
        orgBean = new OrgBean(7, 3, "根目录3-1");
        mDatas2.add(orgBean);
        orgBean = new OrgBean(8, 3, "根目录3-2");
        mDatas2.add(orgBean);
    }
}
