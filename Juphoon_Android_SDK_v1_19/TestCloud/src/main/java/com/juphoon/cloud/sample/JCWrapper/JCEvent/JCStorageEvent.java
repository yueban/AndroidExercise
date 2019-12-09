package com.juphoon.cloud.sample.JCWrapper.JCEvent;

import com.juphoon.cloud.JCStorageItem;

/**
 * Created by maikireton on 2017/12/1.
 */

public class JCStorageEvent extends JCEvent {

    public JCStorageItem item;

    public JCStorageEvent(JCStorageItem item) {
        super(EventType.STORAGE);
        this.item = item;
    }

}
