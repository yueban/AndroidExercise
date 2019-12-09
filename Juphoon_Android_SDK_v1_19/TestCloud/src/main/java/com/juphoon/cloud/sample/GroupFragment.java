package com.juphoon.cloud.sample;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.juphoon.cloud.JCGroup;
import com.juphoon.cloud.JCGroupItem;
import com.juphoon.cloud.JCGroupMember;
import com.juphoon.cloud.JCMessageChannel;
import com.juphoon.cloud.JCMessageChannelItem;
import com.juphoon.cloud.sample.JCWrapper.JCData.JCGroupData;
import com.juphoon.cloud.sample.JCWrapper.JCData.JCMessageData;
import com.juphoon.cloud.sample.JCWrapper.JCEvent.JCEvent;
import com.juphoon.cloud.sample.JCWrapper.JCManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment implements View.OnClickListener {

    class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

        @Override
        public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            GroupViewHolder holder = new GroupViewHolder(
                    LayoutInflater.from(getActivity()).inflate(R.layout.view_group_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(GroupViewHolder holder, int position) {
            if (position >= JCGroupData.listGroups.size()) {
                return;
            }
            JCGroupItem item = JCGroupData.listGroups.get(position);
            List<JCGroupMember> members = JCGroupData.getGroupMembers(item.groupId);
            holder.name.setText(item.name + "\n" + genMembers(members));
            holder.itemView.setSelected(TextUtils.equals(mSelGroupId, item.groupId));
            holder.itemView.setTag(item);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JCGroupItem item = (JCGroupItem) v.getTag();
                    if (!TextUtils.equals(mSelGroupId, item.groupId)) {
                        mSelGroupId = item.groupId;
                        mListGroups.getAdapter().notifyDataSetChanged();
                    }

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    JCGroupItem item = (JCGroupItem) v.getTag();
                    Intent intent = new Intent(getActivity(), IMActivity.class);
                    intent.putExtra(IMActivity.TYPE, JCMessageChannel.TYPE_GROUP);
                    intent.putExtra(IMActivity.KEYID, item.groupId);
                    startActivity(intent);
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return JCGroupData.listGroups.size();
        }

        private String genMembers(List<JCGroupMember> members) {
            StringBuilder builder = new StringBuilder();
            for (JCGroupMember member : members) {
                builder.append(member.userId)
                .append("&")
                .append(member.displayName)
                .append("&")
                .append(getMemberString(member.memberType));
                builder.append(",");
            }
            return builder.toString();
        }

        class GroupViewHolder extends RecyclerView.ViewHolder {

            TextView name;

            public GroupViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.txtName);
            }
        }
    }

    RecyclerView mListGroups;
    EditText mTxtUserId;
    String mSelGroupId;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_group, container, false);
        rootView.findViewById(R.id.btnFetchGroupList).setOnClickListener(this);
        rootView.findViewById(R.id.btnFetchGroupInfo).setOnClickListener(this);
        rootView.findViewById(R.id.btnCreate).setOnClickListener(this);
        rootView.findViewById(R.id.btnDissolve).setOnClickListener(this);
        rootView.findViewById(R.id.btnLeave).setOnClickListener(this);
        rootView.findViewById(R.id.btnAddMember).setOnClickListener(this);
        rootView.findViewById(R.id.btnKickMember).setOnClickListener(this);
        rootView.findViewById(R.id.btnModifyDisplayName).setOnClickListener(this);
        rootView.findViewById(R.id.btnModifyGroupName).setOnClickListener(this);
        rootView.findViewById(R.id.btnSendMessage).setOnClickListener(this);
        mTxtUserId = (EditText) rootView.findViewById(R.id.txtUserId);
        mListGroups = (RecyclerView) rootView.findViewById(R.id.listGroups);
        mListGroups.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListGroups.setAdapter(new GroupAdapter());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListGroups.getAdapter().notifyDataSetChanged();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(JCEvent event) {
        if (event.getEventType() == JCEvent.EventType.GROUP_LIST ||
                event.getEventType() == JCEvent.EventType.GROUP_INFO) {
            mListGroups.getAdapter().notifyDataSetChanged();
            if (!JCGroupData.mapGroupMembers.containsKey(mSelGroupId)) {
                mSelGroupId = null;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnFetchGroupList:
                onFetch(v);
                break;
            case R.id.btnFetchGroupInfo:
                onFetchInfo(v);
                break;
            case R.id.btnCreate:
                onCreate(v);
                break;
            case R.id.btnDissolve:
                onDissolve(v);
                break;
            case R.id.btnLeave:
                onLeave(v);
                break;
            case R.id.btnAddMember:
                onAddMember(v);
                break;
            case R.id.btnKickMember:
                onKickMember(v);
                break;
            case R.id.btnModifyDisplayName:
                onModifyDisplayName(v);
                break;
            case R.id.btnModifyGroupName:
                onModifyGroupName(v);
                break;
            case R.id.btnSendMessage:
                onSendMessage(v);
                break;
            default:
                break;
        }
    }

    public void onFetch(View view) {
        JCManager.getInstance().group.fetchGroups(JCGroupData.gourpListUpdateTime);
    }

    public void onFetchInfo(View view) {
        if (!TextUtils.isEmpty(mSelGroupId)) {
            JCManager.getInstance().group.fetchGroupInfo(mSelGroupId, JCGroupData.getFetchGroupInfoLastTime(mSelGroupId));
        } else {
            Toast.makeText(getActivity(), "请选择群", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCreate(View view) {
        String content = mTxtUserId.getText().toString();
        String[] userIds = content.split(";");
        if (userIds.length == 0) {
            mTxtUserId.setError(getString(R.string.error_field_required));
            return;
        }
        List<JCGroupMember> members = new ArrayList<>();
        for (String userId: userIds) {
            members.add(new JCGroupMember(null, userId, userId, JCGroup.GROUP_MEMBER_TYPE_MEMBER, JCGroup.GROUP_CHANGE_STATE_ADD));
        }
        JCManager.getInstance().group.createGroup(members, "test");
    }

    public void onLeave(View view) {
        if (!TextUtils.isEmpty(mSelGroupId)) {
            JCManager.getInstance().group.leave(mSelGroupId);
        } else {
            Toast.makeText(getActivity(), "请选择群", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDissolve(View view) {
        if (!TextUtils.isEmpty(mSelGroupId)) {
            JCManager.getInstance().group.dissolve(mSelGroupId);
        } else {
            Toast.makeText(getActivity(), "请选择群", Toast.LENGTH_SHORT).show();
        }
    }

    public void onAddMember(View view) {
        String content = mTxtUserId.getText().toString();
        String[] userIds = content.split(";");
        if (userIds.length == 0) {
            mTxtUserId.setError(getString(R.string.error_field_required));
            return;
        }
        if (TextUtils.isEmpty(mSelGroupId)) {
            Toast.makeText(getActivity(), "请选择群", Toast.LENGTH_SHORT).show();
            return;
        }
        List<JCGroupMember> members = new ArrayList<>();
        for (String userId: userIds) {
            members.add(new JCGroupMember(mSelGroupId, userId, userId, JCGroup.GROUP_MEMBER_TYPE_MEMBER, JCGroup.GROUP_CHANGE_STATE_ADD));
        }
        JCManager.getInstance().group.dealMembers(mSelGroupId, members);
    }

    public void onKickMember(View view) {
        String content = mTxtUserId.getText().toString();
        String[] userIds = content.split(";");
        if (userIds.length == 0) {
            mTxtUserId.setError(getString(R.string.error_field_required));
            return;
        }
        if (TextUtils.isEmpty(mSelGroupId)) {
            Toast.makeText(getActivity(), "请选择群", Toast.LENGTH_SHORT).show();
            return;
        }
        List<JCGroupMember> members = new ArrayList<>();
        for (String userId: userIds) {
            members.add(new JCGroupMember(mSelGroupId, userId, userId, JCGroup.GROUP_MEMBER_TYPE_MEMBER, JCGroup.GROUP_CHANGE_STATE_REMOVE));
        }
        JCManager.getInstance().group.dealMembers(mSelGroupId, members);
    }

    public void onModifyDisplayName(View view) {
        String content = mTxtUserId.getText().toString();
        if (TextUtils.isEmpty(content)) {
            mTxtUserId.setError(getString(R.string.error_field_required));
            return;
        }
        if (TextUtils.isEmpty(mSelGroupId)) {
            Toast.makeText(getActivity(), "请选择群", Toast.LENGTH_SHORT).show();
            return;
        }
        JCGroupMember self = JCGroupData.getGroupMember(mSelGroupId, JCManager.getInstance().client.getUserId());
        JCManager.getInstance().group.updateSelfInfo(
                new JCGroupMember(mSelGroupId, self.userId, content, self.memberType, JCGroup.GROUP_CHANGE_STATE_UPDATE));
    }

    public void onModifyGroupName(View view) {
        String content = mTxtUserId.getText().toString();
        if (TextUtils.isEmpty(content)) {
            mTxtUserId.setError(getString(R.string.error_field_required));
            return;
        }
        if (TextUtils.isEmpty(mSelGroupId)) {
            Toast.makeText(getActivity(), "请选择群", Toast.LENGTH_SHORT).show();
            return;
        }
        JCManager.getInstance().group.updateGroup(new JCGroupItem(mSelGroupId, content, JCGroup.GROUP_CHANGE_STATE_UPDATE));
    }

    public void onSendMessage(View view) {
        String content = mTxtUserId.getText().toString();
        if (TextUtils.isEmpty(content)) {
            mTxtUserId.setError(getString(R.string.error_field_required));
            return;
        }
        if (TextUtils.isEmpty(mSelGroupId)) {
            Toast.makeText(getActivity(), "请选择群", Toast.LENGTH_SHORT).show();
            return;
        }
        JCMessageChannelItem item = JCManager.getInstance().messageChannel.sendMessage(JCMessageChannel.TYPE_GROUP, mSelGroupId, "text", content, null);
        if (item != null) {
            JCMessageData.addMessage(item);
            EventBus.getDefault().post(new JCEvent(JCEvent.EventType.MESSAGE));
            Toast.makeText(getActivity(), "Send Ok, View Messages in Message(Tab)", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Send Fail", Toast.LENGTH_SHORT).show();
        }
    }

    private String getMemberString(int memberType) {
        switch (memberType) {
            case JCGroup.GROUP_MEMBER_TYPE_OWNER:
                return "Owner";
            case JCGroup.GROUP_MEMBER_TYPE_MANAGER:
                return "Manager";
            default:
                return "Member";
        }
    }

}
