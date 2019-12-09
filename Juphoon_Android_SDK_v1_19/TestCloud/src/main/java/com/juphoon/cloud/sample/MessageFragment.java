package com.juphoon.cloud.sample;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.juphoon.cloud.JCMessageChannel;
import com.juphoon.cloud.JCMessageChannelItem;
import com.juphoon.cloud.sample.JCWrapper.JCData.JCMessageData;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCEvent;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCMessageEvent;
import com.juphoon.cloud.sample.JCWrapper.JCManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Locale;

public class MessageFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText mTxtUserId;
    private EditText mTxtFileType;
    private EditText mTxtContent;
    private MessageAdapter mMessageAdapter;

    public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_message_item,viewGroup,false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            JCMessageChannelItem item = JCMessageData.getTotalMessages().get(i);
            String keyId = item.getType() == JCMessageChannel.TYPE_1TO1 ? item.getUserId() : item.getGroupId();
            String content;
            if (!TextUtils.isEmpty(item.getFileUri())) {
                content = String.format(Locale.getDefault(), "%s:[%s][%s]%s%s",
                        item.getDirection() == JCMessageChannel.DIRECTION_SEND ? "me" : item.getDisplayName(), keyId, item.getMessageType(), item.getFileUri(), item.getExtraParams() != null ? "[has extra]" : "");
            } else {
                content = String.format(Locale.getDefault(), "%s:[%s][%s]%s%s",
                        item.getDirection() == JCMessageChannel.DIRECTION_SEND ? "me" : item.getDisplayName(), keyId, item.getMessageType(), item.getText(), item.getExtraParams() != null ? "[has extra]" : "");
            }
            viewHolder.mContent.setText(content);
        }

        @Override
        public int getItemCount() {
            return JCMessageData.getTotalMessages().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView mContent;

            public ViewHolder(View view){
                super(view);
                mContent = (TextView) view.findViewById(R.id.txtContent);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        mTxtUserId = (EditText) rootView.findViewById(R.id.editTextUserId);
        mTxtContent = (EditText) rootView.findViewById(R.id.editTextContent);
        mTxtFileType = (EditText) rootView.findViewById(R.id.editTextFileType);
        rootView.findViewById(R.id.btnSendText).setOnClickListener(this);
        rootView.findViewById(R.id.btnSendFile).setOnClickListener(this);
        rootView.findViewById(R.id.btnClear).setOnClickListener(this);
        RecyclerView listMessages = (RecyclerView) rootView.findViewById(R.id.listMessages);
        listMessages.setLayoutManager(new LinearLayoutManager(getActivity()));
        listMessages.setHasFixedSize(true);
        mMessageAdapter = new MessageAdapter();
        listMessages.setAdapter(mMessageAdapter);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(JCEvent event) {
        if (event.getEventType() == JCEvent.EventType.MESSAGE) {
            JCMessageEvent messageEvent = (JCMessageEvent)event;
            if (!messageEvent.send) {
                JCMessageData.addMessage(messageEvent.item);
            }
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSendText:
                onSendText(v);
                break;
            case R.id.btnSendFile:
                onSendFile(v);
                break;
            case R.id.btnClear:
                onClear(v);
                break;
            default:
                break;
        }
    }

    public void onSendText(View view) {
        mTxtUserId.setError(null);
        mTxtContent.setError(null);
        String userId = mTxtUserId.getText().toString();
        String content = mTxtContent.getText().toString();
        if (TextUtils.isEmpty(userId)) {
            mTxtUserId.setError(getString(R.string.error_field_required));
            return;
        }
        if (TextUtils.isEmpty(content)) {
            mTxtContent.setError(getString(R.string.error_field_required));
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("key1", "value1");
        JCMessageChannelItem item = JCManager.getInstance().messageChannel.sendMessage(JCMessageChannel.TYPE_1TO1, userId, "Text", content, params);
        if (item == null) {
            Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT).show();
        } else {
            mTxtContent.setText("");
            JCMessageData.addMessage(item);
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    public void onSendFile(View view) {
        mTxtUserId.setError(null);
        mTxtContent.setError(null);
        String userId = mTxtUserId.getText().toString();
        String content = mTxtContent.getText().toString();
        String fileType= mTxtFileType.getText().toString();
        if (TextUtils.isEmpty(userId)) {
            mTxtUserId.setError(getString(R.string.error_field_required));
            return;
        }
        if (TextUtils.isEmpty(fileType)) {
            mTxtFileType.setError(getString(R.string.error_field_required));
            return;
        }
        if (TextUtils.isEmpty(content)) {
            mTxtContent.setError(getString(R.string.error_field_required));
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("key1", "value1");
        JCMessageChannelItem item = JCManager.getInstance().messageChannel.sendFile(JCMessageChannel.TYPE_1TO1, userId, fileType, content, null/*缩略图路径*/, 0/*文件大小*/, 0/*时长*/, params);
        if (item == null) {
            Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT).show();
        } else {
            mTxtContent.setText("");
            JCMessageData.addMessage(item);
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    public void onClear(View view) {
        JCMessageData.clear();
        mMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        JCManager.getInstance().call.setConference(isChecked);
        getView().findViewById(R.id.btnVideoCall).setEnabled(!isChecked);
    }
}
