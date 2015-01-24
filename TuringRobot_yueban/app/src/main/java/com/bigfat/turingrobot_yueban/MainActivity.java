package com.bigfat.turingrobot_yueban;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bigfat.turingrobot_yueban.bin.ChatMessage;
import com.bigfat.turingrobot_yueban.utils.HttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private ListView listView;
    private ChatMessageAdapter adapter;
    private List<ChatMessage> datas;

    private EditText inputMsg;
    private Button sendMsg;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //将接收的消息添加到主界面
            ChatMessage fromMessage = (ChatMessage) msg.obj;
            datas.add(fromMessage);
            adapter.notifyDataSetChanged();
            listView.setSelection(adapter.getCount() - 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        initView();
        initDatas();
        initListener();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_view);
        inputMsg = (EditText) findViewById(R.id.input_message);
        sendMsg = (Button) findViewById(R.id.send_message);
    }

    private void initDatas() {
        datas = new ArrayList<>();
        datas.add(new ChatMessage("你好，月半兄为您服务", ChatMessage.Type.INCOMING, new Date()));
        adapter = new ChatMessageAdapter(this, datas);
        listView.setAdapter(adapter);
    }

    private void initListener() {
        sendMsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_message:
                final String toMsg = inputMsg.getText().toString().trim();
                //清空输入框
                inputMsg.setText("");
                if (TextUtils.isEmpty(toMsg)) {
                    Toast.makeText(MainActivity.this, "发送消息不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //将发送消息添加到界面
                ChatMessage toMessage = new ChatMessage(toMsg, ChatMessage.Type.OUTCOMING, new Date());
                datas.add(toMessage);
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);

                //开启线程发送消息，获取返回消息
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
                        Message m = Message.obtain();
                        m.obj = fromMessage;
                        mHandler.sendMessage(m);
                    }
                }).start();
                break;
        }
    }
}
