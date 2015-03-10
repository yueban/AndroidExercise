package com.bigfat.bmobtest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private String bmobObjectId = "e5bc2e4e4f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBmob();

//        insert();
//        query();
        update();
//        query();
//        delete();
//        query();
    }

    private void initBmob() {
        Bmob.initialize(MainActivity.this, C.BMOB_APPLICATION_ID);
    }

    private void insert() {
        final Person p = new Person();
        p.setName("小明");
        p.setAddress("上海");
        p.save(MainActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                bmobObjectId = p.getObjectId();
                Log.i(TAG, "添加数据成功，返回objectId：" + p.getObjectId());
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.i(TAG, "创建数据失败：" + msg);
            }
        });
    }

    private void query() {
        BmobQuery<Person> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(MainActivity.this, bmobObjectId, new GetListener<Person>() {

            @Override
            public void onSuccess(Person person) {
                Log.i(TAG, "person--->" + person);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.i(TAG, "code--->" + code + "\tmsg--->" + msg);
            }
        });
    }

    private void update() {
        //更新Person表里面id为6b6c11c537的数据，address内容更新为“北京朝阳”
        final Person p = new Person();
        p.setAddress("北京朝阳");
        p.update(this, bmobObjectId, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "更新成功：" + p.getUpdatedAt());
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.i(TAG, "更新失败：" + msg);
            }
        });
    }

    private void delete() {
        Person p = new Person();
        p.setObjectId(bmobObjectId);
        p.delete(this, new DeleteListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "删除成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.i(TAG, "删除失败：" + msg);
            }
        });
    }
}