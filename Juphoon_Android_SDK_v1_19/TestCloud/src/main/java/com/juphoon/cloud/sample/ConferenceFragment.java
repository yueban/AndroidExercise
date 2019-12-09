package com.juphoon.cloud.sample;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.juphoon.cloud.JCMediaChannel;
import com.juphoon.cloud.sample.JCWrapper.JCConfUtils;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCConfQueryEvent;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCEvent;
import com.juphoon.cloud.sample.JCWrapper.JCManager;
import com.juphoon.cloud.sample.Toos.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ConferenceFragment extends Fragment implements View.OnClickListener {

    private EditText mEditChannelId;
    private EditText mEditPassword;
    private TextView mTxtInfo;
    private Button mBtnJoin;
    private Button mBtnPstnCall;
    private Switch mSwitchUriMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conference, container, false);
        mEditChannelId = (EditText) view.findViewById(R.id.editChannelId);
        mEditPassword = view.findViewById(R.id.editPassword);
        mTxtInfo = (TextView) view.findViewById(R.id.txtInfo);

        mBtnJoin = (Button) view.findViewById(R.id.btnJoin);
        mBtnJoin.setOnClickListener(this);
        view.findViewById(R.id.btnQuery).setOnClickListener(this);
        mBtnPstnCall = (Button) view.findViewById(R.id.btnPstnCall);
        mBtnPstnCall.setOnClickListener(this);
        ((CheckBox)view.findViewById(R.id.checkPstnCall)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                JCManager.getInstance().pstnMode = isChecked;
                mBtnPstnCall.setEnabled(isChecked);
                mBtnJoin.setEnabled(!isChecked);
            }
        });
        mSwitchUriMode = view.findViewById(R.id.switchUriMode);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(JCEvent event) {
        if (event.getEventType() == JCEvent.EventType.CONFERENCE_QUERY) {
            JCConfQueryEvent queryEvent = (JCConfQueryEvent) event;
            if (queryEvent.result) {
                mTxtInfo.setText(String.format(Locale.getDefault(), "查询成功 会议名:%s 会议号:%d 人数:%d 成员:%s",
                        queryEvent.queryInfo.getChannelId(), queryEvent.queryInfo.getNumber(),
                        queryEvent.queryInfo.getClientCount(), queryEvent.queryInfo.getMembers().toString()));
            } else {
                mTxtInfo.setText(String.format(Locale.getDefault(), "查询失败"));
            }
        }
    }

    public void onJoin(View view) {
        mEditChannelId.setError(null);
        String channelId = mEditChannelId.getText().toString();
        String password = mEditPassword.getText().toString();
        if (!TextUtils.isEmpty(channelId)) {
            String cdnAddress = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getContext().getString(R.string.cloud_setting_key_conference_cdn_address), "");
            Map<String, String> param = new HashMap<>();
            if (!TextUtils.isEmpty(password)) {
                param.put(JCMediaChannel.JOIN_PARAM_PASSWORD, password);
            }
            if (!TextUtils.isEmpty(cdnAddress)) {
                param.put(JCMediaChannel.JOIN_PARAM_CDN, cdnAddress);
            }
            boolean qiniuEnabled = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(getContext().getString(R.string.cloud_setting_key_conference_qiniu_enable), false);
            String bucketName = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getContext().getString(R.string.cloud_setting_key_conference_qiniu_bucket_name), "");
            String secretKey = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getContext().getString(R.string.cloud_setting_key_conference_qiniu_secret_key), "");
            String accessKey = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getContext().getString(R.string.cloud_setting_key_conference_qiniu_access_key), "");
            String fileName = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getContext().getString(R.string.cloud_setting_key_conference_qiniu_file_name), "");
            if (qiniuEnabled && !TextUtils.isEmpty(bucketName) && !TextUtils.isEmpty(secretKey)
                    && !TextUtils.isEmpty(accessKey) && !TextUtils.isEmpty(fileName)) {
                param.put(JCMediaChannel.JOIN_PARAM_RECORD, JCConfUtils.qiniuRecordParam(true, bucketName, secretKey, accessKey, fileName));
            }
            param.put(JCMediaChannel.JOIN_PARAM_SMOOTH_MODE, Boolean.toString(true));
            //param.put(JCMediaChannel.JOIN_PARAM_REGION, String.valueOf(JCMediaChannel.REGION_CHINA));
            boolean enableUploadAudio = PreferenceManager.getDefaultSharedPreferences(
                    getContext()).getBoolean(getContext().getString(R.string.cloud_setting_key_conference_audio_enable), false);
            JCManager.getInstance().mediaChannel.enableUploadAudioStream(enableUploadAudio);
            boolean enableUploadVideo = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(getContext().getString(R.string.cloud_setting_key_conference_video_enable), true);
            JCManager.getInstance().mediaChannel.enableUploadVideoStream(enableUploadVideo);
            String maxResolution = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getContext().getString(R.string.cloud_setting_key_conference_max_resolution), "0");
            param.put(JCMediaChannel.JOIN_PARAM_MAX_RESOLUTION, maxResolution);
            if (mSwitchUriMode.isChecked()) {
                param.put(JCMediaChannel.JOIN_PARAM_URI_MODE, "true");
            }
            if (JCManager.getInstance().mediaChannel.join(channelId, param)) {
                Utils.hideKeyboard(getActivity(), mEditChannelId);
                startActivity(new Intent(getActivity(), ConferenceActivity.class));
            }
        } else {
            mEditChannelId.setError(getString(R.string.error_field_required));
        }
    }

    public void onQuery(View view) {
        mEditChannelId.setError(null);
        String channelId = mEditChannelId.getText().toString();
        if (!TextUtils.isEmpty(channelId)) {
            JCManager.getInstance().mediaChannel.query(channelId);
            Utils.hideKeyboard(getActivity(), mEditChannelId);
        } else {
            mEditChannelId.setError(getString(R.string.error_field_required));
        }
    }

    public void onPstn(View view) {
        mEditChannelId.setError(null);
        String channelId = mEditChannelId.getText().toString();
        if (!TextUtils.isEmpty(channelId)) {
            JCManager.getInstance().mediaChannel.enableUploadVideoStream(false);
            JCManager.getInstance().mediaChannel.enableUploadAudioStream(true);
            if (JCManager.getInstance().mediaChannel.join(channelId, null)) {
                Utils.hideKeyboard(getActivity(), mEditChannelId);
                startActivity(new Intent(getActivity(), ConferenceActivity.class));
            }
        } else {
            mEditChannelId.setError(getString(R.string.error_field_required));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnJoin:
                onJoin(v);
                break;
            case R.id.btnQuery:
                onQuery(v);
                break;
            case R.id.btnPstnCall:
                onPstn(v);
                break;
            default:
                break;
        }
    }
}
