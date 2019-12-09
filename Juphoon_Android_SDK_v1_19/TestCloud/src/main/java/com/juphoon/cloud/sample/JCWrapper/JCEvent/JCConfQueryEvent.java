package com.juphoon.cloud.sample.JCWrapper.JCEvent;

import com.juphoon.cloud.JCMediaChannel;
import com.juphoon.cloud.JCMediaChannelQueryInfo;

public class JCConfQueryEvent extends JCEvent {

    public int operationId;
    public boolean result;
    public int reason;
    public JCMediaChannelQueryInfo queryInfo;

    public JCConfQueryEvent(int operationId, boolean result, @JCMediaChannel.MediaChannelReason int reason, JCMediaChannelQueryInfo queryInfo) {
        super(EventType.CONFERENCE_QUERY);
        this.operationId = operationId;
        this.result = result;
        this.reason = reason;
        this.queryInfo = queryInfo;
    }

}
