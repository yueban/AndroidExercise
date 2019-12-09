package com.juphoon.cloud.sample.JCWrapper.JCEvent;

import com.juphoon.cloud.JCCallItem;

public class JCCallMessageEvent extends JCEvent {

    public String type;
    public String content;
    public JCCallItem callItem;

    public JCCallMessageEvent(String type, String content, JCCallItem item) {
        super(EventType.CALL_MESSAGE_RECEIVED);
        this.type = type;
        this.content = content;
        this.callItem = item;
    }
}
