package com.juphoon.cloud.sample.JCWrapper.JCEvent;

import com.juphoon.cloud.JCMediaChannel;

public class JCJoinEvent extends JCEvent {

    public boolean result;
    @JCMediaChannel.MediaChannelReason
    public int reason;
    public String channelId;

    public JCJoinEvent(boolean result, @JCMediaChannel.MediaChannelReason int reason, String channelId) {
        super(EventType.CONFERENCE_JOIN);
        this.result = result;
        this.reason = reason;
        this.channelId = channelId;
    }
}
