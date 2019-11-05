package com.yueban.contactmimetype;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Uri data = getIntent().getData();
        if (data == null) {
            showError();
            return;
        }
        Log.d("view activity", data.toString());

        Cursor cursor = getContentResolver().query(data, null, null, null, null);
        if (cursor == null) {
            showError();
            return;
        }
        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DATA1));

            ((TextView) findViewById(R.id.content)).setText(String.format("密信界面\n姓名: %s\n电话: %s", name, phone));
        }
    }

    private void showError() {
        Toast.makeText(this, "未获取到联系人数据", Toast.LENGTH_SHORT).show();
    }
}
