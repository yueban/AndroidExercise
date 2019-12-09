package com.juphoon.cloud.sample.JCWrapper.JCEvent;

import com.juphoon.cloud.JCMediaChannel;

public class JCConfStopEvent extends JCEvent{

    public boolean result;
    public @JCMediaChannel.MediaChannelReason int reason;

    public JCConfStopEvent(boolean result, @JCMediaChannel.MediaChannelReason int reason) {
        super(JCEvent.EventType.CONFERENCE_STOP);
        this.result = result;
        this.reason = reason;
    }
}
