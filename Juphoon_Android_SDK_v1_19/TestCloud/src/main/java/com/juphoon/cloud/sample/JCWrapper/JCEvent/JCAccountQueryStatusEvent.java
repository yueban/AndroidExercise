package com.juphoon.cloud.sample.JCWrapper.JCEvent;

import com.juphoon.cloud.JCAccountItem;

import java.util.List;

public class JCAccountQueryStatusEvent extends JCEvent {

    public boolean queryResult;
    public List<JCAccountItem> accountItemList;

    public JCAccountQueryStatusEvent(boolean result, List<JCAccountItem> accountItemList) {
        super(EventType.ACCOUNT_QUERY_USER_STATUS);
        this.queryResult = result;
        this.accountItemList = accountItemList;
    }
}
