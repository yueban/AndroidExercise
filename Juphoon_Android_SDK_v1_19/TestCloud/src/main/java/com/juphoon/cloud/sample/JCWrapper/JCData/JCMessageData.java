package com.juphoon.cloud.sample.JCWrapper.JCData;

import android.text.TextUtils;

import com.juphoon.cloud.JCMessageChannel;
import com.juphoon.cloud.JCMessageChannelItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCMessageData {

    private static List<JCMessageChannelItem> listMessages = new ArrayList<>();
    private static Map<String, List<JCMessageChannelItem>> mapMessages = new HashMap<>();

    public static List<JCMessageChannelItem> getTotalMessages() {
        return listMessages;
    }

    public static void addMessage(JCMessageChannelItem item) {
        listMessages.add(item);
        List<JCMessageChannelItem> messages;
        String keyId = item.getType() == JCMessageChannel.TYPE_1TO1 ? item.getUserId() : item.getGroupId();
        if (mapMessages.containsKey(keyId)) {
            messages = mapMessages.get(keyId);
        } else {
            messages = new ArrayList<>();
            mapMessages.put(keyId, messages);
        }
        messages.add(item);
    }

    public static List<JCMessageChannelItem> getMessages(String keyId) {
        if (mapMessages.containsKey(keyId)) {
            return mapMessages.get(keyId);
        } else {
            List<JCMessageChannelItem> messages = new ArrayList<>();
            mapMessages.put(keyId, messages);
            return messages;
        }
    }

    public static void removeMessages(String keyId) {
        mapMessages.remove(keyId);
        for (int i=0; i<listMessages.size(); ) {
            JCMessageChannelItem item = listMessages.get(i);
            String itemKeyId = item.getType() == JCMessageChannel.TYPE_1TO1 ? item.getUserId() : item.getGroupId();
            if (TextUtils.equals(itemKeyId, keyId)) {
                listMessages.remove(i);
            } else {
                i++;
            }
        }
    }

    public static void clear() {
        mapMessages.clear();
        listMessages.clear();
    }

}
