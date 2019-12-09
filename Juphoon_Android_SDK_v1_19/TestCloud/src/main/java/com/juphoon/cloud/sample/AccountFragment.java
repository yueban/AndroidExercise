package com.juphoon.cloud.sample;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.juphoon.cloud.JCAccount;
import com.juphoon.cloud.JCAccountItem;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCAccountQueryStatusEvent;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCEvent;
import com.juphoon.cloud.sample.JCWrapper.JCManager;
import com.juphoon.cloud.sample.Toos.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.List;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private EditText mTxtUserId;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        mTxtUserId = rootView.findViewById(R.id.editTextUserId);
        rootView.findViewById(R.id.queryUserStatus).setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.queryUserStatus:
                String userID = mTxtUserId.getText().toString();
                List<String> queryStatusUserIds = Arrays.asList(userID.split(","));
                if (!TextUtils.isEmpty(userID)) {
                    Utils.hideKeyboard(getActivity(), mTxtUserId);
                    JCManager.getInstance().account.queryUserStatus(queryStatusUserIds);
                } else {
                    mTxtUserId.setError(getString(R.string.error_field_required));
                }
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onEvent(JCEvent event) {
        if (event.getEventType() == JCEvent.EventType.ACCOUNT_QUERY_USER_STATUS) {
            JCAccountQueryStatusEvent accountQueryStatusEvent = (JCAccountQueryStatusEvent) event;
            if (accountQueryStatusEvent.queryResult) {
                List<JCAccountItem> accountItemList = ((JCAccountQueryStatusEvent) event).accountItemList;
                StringBuffer stringBuffer = new StringBuffer();
                for (JCAccountItem accountItem : accountItemList) {
                    stringBuffer.append(accountItem.userId + ":");
                    String status = "";
                    switch (accountItem.status) {
                        case JCAccount.ACCOUNT_USER_STATUS_ERR: {
                            status = "查询账号出错";
                            break;
                        }
                        case JCAccount.ACCOUNT_USER_STATUS_NOT_FOUND: {
                            status = "账号未注册";
                            break;
                        }
                        case JCAccount.ACCOUNT_USER_STATUS_OFFLINE: {
                            status = "offline状态";
                            break;
                        }
                        case JCAccount.ACCOUNT_USER_STATUS_ONLINE: {
                            status = "在线状态";
                            break;
                        }
                        case JCAccount.ACCOUNT_USER_STATUS_PUSH: {
                            status = "push状态";
                            break;
                        }
                        default:
                            break;
                    }
                    stringBuffer.append(status + "; ");
                }
                Toast.makeText(getContext(), stringBuffer, Toast.LENGTH_LONG).show();
            } else {

            }
        }
    }
}
