package com.bigfat.androidliteormtest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.bigfat.androidliteormtest.model.User;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBase;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    DataBase db;
    private Button btnInsert;
    private Button btnDelete;
    private Button btnQuery;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (db == null) {
            db = LiteOrm.newInstance(this, "data.db");
        }

        initView();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        if(db!=null){
            db.close();
        }
        super.onDestroy();
    }

    private void initView() {
        btnInsert = (Button) findViewById(R.id.btn_insert);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnQuery = (Button) findViewById(R.id.btn_query);
        btnUpdate = (Button) findViewById(R.id.btn_update);
    }

    private void initEvent() {
        btnInsert.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                testInsert();
                break;

            case R.id.btn_delete:
                testDelete();
                break;

            case R.id.btn_query:
                testQuery();
                break;

            case R.id.btn_update:
                testUpdate();
                break;
        }
    }

    private void testInsert() {
        User uPeter = new User("Peter", "22", 0);
        User uBigPeter = new User("BigPeter", "30", 1);
        List<User> users  =new ArrayList<>();
        users.add(uPeter);
        users.add(uBigPeter);
        db.insert(users);

//        db.insert(uPeter);
//        db.insert(uBigPeter);
    }

    private void testDelete() {
        db.delete(User.class, WhereBuilder.create().equals("name","Peter"));
    }

    private void testQuery(){
        List<User> users = db.queryAll(User.class);
        for(User user : users){
//            Log.i(user);
        }
    }

    private void testUpdate() {
        List<User> users = db.queryAll(User.class);
        users.get(0).setAge("100");
        db.save(users.get(0));
    }
}
