package com.juphoon.cloud.sample.JCWrapper.JCEvent;

public class JCConfMessageEvent extends JCEvent {

    public String type;
    public String content;
    public String fromUserId;

    public JCConfMessageEvent(String type, String content, String fromUserId) {
        super(EventType.CONFERENCE_MESSAGE_RECEIVED);
        this.type = type;
        this.content = content;
        this.fromUserId = fromUserId;
    }

}
