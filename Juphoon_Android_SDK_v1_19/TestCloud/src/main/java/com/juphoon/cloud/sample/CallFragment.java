package com.juphoon.cloud.sample;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.juphoon.cloud.sample.JCWrapper.JCManager;
import com.juphoon.cloud.sample.Toos.Utils;

import java.util.Locale;

public class CallFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText mTxtUserId;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_call, container, false);
        mTxtUserId = (EditText) rootView.findViewById(R.id.editTextUserId);
        rootView.findViewById(R.id.btnAudioCall).setOnClickListener(this);
        rootView.findViewById(R.id.btnVideoCall).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TextView) getView().findViewById(R.id.txtCallSetting)).setText(
                String.format(Locale.getDefault(), getString(R.string.call_max_call_setting_fmt),
                        JCManager.getInstance().call.maxCallNum,
                        JCManager.getInstance().call.getConference()));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnAudioCall:
                onAudioCall(v);
                break;
            case R.id.btnVideoCall:
                onVideoCall(v);
                break;
            default:
                break;
        }
    }

    public void onAudioCall(View view) {
        mTxtUserId.setError(null);
        String userId = mTxtUserId.getText().toString();
        if (!TextUtils.isEmpty(userId)) {
            if (JCManager.getInstance().call.call(userId, false, "AndroidTest")) {
                Utils.hideKeyboard(getActivity(), mTxtUserId);
            } else {
                mTxtUserId.setError(getString(R.string.error_call_function));
            }
        } else {
            mTxtUserId.setError(getString(R.string.error_field_required));
        }
    }

    public void onVideoCall(View view) {
        mTxtUserId.setError(null);
        String userId = mTxtUserId.getText().toString();
        if (!TextUtils.isEmpty(userId)) {
            if (JCManager.getInstance().call.call(userId, true, "AndroidTest")) {
                Utils.hideKeyboard(getActivity(), mTxtUserId);
            } else {
                mTxtUserId.setError(getString(R.string.error_call_function));
            }
        } else {
            mTxtUserId.setError(getString(R.string.error_field_required));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        JCManager.getInstance().call.setConference(isChecked);
        getView().findViewById(R.id.btnVideoCall).setEnabled(!isChecked);
    }
}
