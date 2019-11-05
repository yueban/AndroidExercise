package com.yueban.contactmimetype;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQ_PERMISSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
            new String[] { Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS }, REQ_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "请授予权限", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                String name = ((EditText) findViewById(R.id.et_name)).getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "联系人姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String phone = ((EditText) findViewById(R.id.et_phone)).getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "联系人电话不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContactManager.addContact(this, name, phone);
                break;

            case R.id.btn_clear:
                ContactManager.clearAll(this);
                break;

            default:
                break;
        }
    }
}
