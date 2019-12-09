package com.juphoon.cloud.sample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.juphoon.cloud.JCGroupItem;
import com.juphoon.cloud.JCMessageChannel;
import com.juphoon.cloud.JCMessageChannelItem;
import com.juphoon.cloud.sample.JCWrapper.JCData.JCGroupData;
import com.juphoon.cloud.sample.JCWrapper.JCData.JCMessageData;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCEvent;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCLoginEvent;
import com.juphoon.cloud.sample.JCWrapper.JCManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Locale;

public class IMActivity extends AppCompatActivity {

    public static final String TYPE = "type";
    public static final String KEYID = "keyid";

    class IMAdapter extends RecyclerView.Adapter<IMAdapter.IMViewHolder> {

        @Override
        public IMViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            IMViewHolder holder = new IMViewHolder(
                    LayoutInflater.from(IMActivity.this).inflate(R.layout.view_im_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(IMViewHolder holder, int position) {
            List<JCMessageChannelItem> messages = JCMessageData.getMessages(mKeyId);
            if (position >= messages.size()) {
                return;
            }
            JCMessageChannelItem item = messages.get(position);
            holder.content.setText(formatContent(item));
            holder.itemView.setTag(item);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return JCMessageData.getMessages(mKeyId).size();
        }

        private String formatContent(JCMessageChannelItem item) {
            return String.format(Locale.getDefault(), "dir:%d sender:%s content:%s time:%d",
                    item.getDirection(), item.getUserId(), item.getText(), item.getTime());
        }

        class IMViewHolder extends RecyclerView.ViewHolder {

            TextView content;

            public IMViewHolder(View view) {
                super(view);
                content = (TextView) view.findViewById(R.id.txtContent);
            }
        }
    }

    private EditText mTxtContent;
    @JCMessageChannel.Type
    private int mType;
    private String mKeyId;
    private RecyclerView mListMessages;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im);

        mTxtContent = (EditText) findViewById(R.id.txtContent);
        mKeyId = getIntent().getStringExtra(KEYID);
        mType = getIntent().getIntExtra(TYPE, JCMessageChannel.TYPE_1TO1);

        mListMessages = (RecyclerView) findViewById(R.id.listMessages);
        mListMessages.setLayoutManager(new LinearLayoutManager(this));
        mListMessages.setAdapter(new IMAdapter());

        EventBus.getDefault().register(this);

        setTitle(String.format(Locale.getDefault(), "[%s]%s", mType == JCMessageChannel.TYPE_1TO1 ? "1对1" : "群组", mKeyId));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(JCEvent event) {
        if (event.getEventType() == JCEvent.EventType.MESSAGE) {
            mListMessages.getAdapter().notifyDataSetChanged();
            mListMessages.smoothScrollToPosition(JCMessageData.getMessages(mKeyId).size());
        }
    }

    public void onSend(View view) {
        String content = mTxtContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
        } else {
            JCMessageChannelItem item = JCManager.getInstance().messageChannel.sendMessage(mType, mKeyId, "text", content, null);
            if (item != null) {
                JCMessageData.addMessage(item);
                EventBus.getDefault().post(new JCEvent(JCEvent.EventType.MESSAGE));
                mTxtContent.setText("");
            } else {
                Toast.makeText(this, "发送失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
