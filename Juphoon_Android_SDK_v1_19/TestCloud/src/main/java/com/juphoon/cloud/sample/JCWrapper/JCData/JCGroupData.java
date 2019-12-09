package com.juphoon.cloud.sample.JCWrapper.JCData;

import android.text.TextUtils;

import com.juphoon.cloud.JCGroupItem;
import com.juphoon.cloud.JCGroupMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCGroupData {

    public static long gourpListUpdateTime = 0;

    public final static List<JCGroupItem> listGroups = new ArrayList<>();
    public final static Map<String, List<JCGroupMember>> mapGroupMembers = new HashMap<>();
    public final static Map<String, Long> mapGroupUpdateTime = new HashMap<>();

    public static long getFetchGroupInfoLastTime(String groupId) {
        if (mapGroupUpdateTime.containsKey(groupId)) {
            return mapGroupUpdateTime.get(groupId);
        }
        return 0;
    }

    public static void setFetchGroupInfoLastTime(String groupId, long updateTime) {
        mapGroupUpdateTime.put(groupId, updateTime);
    }

    public static List<JCGroupMember> getGroupMembers(String groupId) {
        if (mapGroupMembers.containsKey(groupId)) {
            return mapGroupMembers.get(groupId);
        } else {
            return new ArrayList<>();
        }
    }

    public static JCGroupMember getGroupMember(String groupId, String userId) {
        if (mapGroupMembers.containsKey(groupId)) {
            List<JCGroupMember> members = mapGroupMembers.get(groupId);
            for (JCGroupMember member : members) {
                if (TextUtils.equals(member.userId, userId)) {
                    return member;
                }
            }
        }
        return null;
    }

    public static void clear() {
        listGroups.clear();
        mapGroupMembers.clear();
        mapGroupUpdateTime.clear();
        gourpListUpdateTime = 0;
    }

}
