package com.juphoon.cloud.sample.JCWrapper.JCEvent;

import com.juphoon.cloud.JCMessageChannelItem;

/**
 * Created by maikireton on 2017/12/1.
 */

public class JCMessageEvent extends JCEvent {

    public boolean send;
    public JCMessageChannelItem item;

    public JCMessageEvent(boolean send, JCMessageChannelItem item) {
        super(EventType.MESSAGE);
        this.send = send;
        this.item = item;
    }

}
