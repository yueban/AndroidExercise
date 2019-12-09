package com.juphoon.cloud.sample.JCWrapper.JCEvent;

public class JCEvent {

    public enum EventType {
        Exit,
        LOGIN,
        LOGOUT,
        CLIENT_STATE_CHANGE,
        CALL_ADD,
        CALL_UPDATE,
        CALL_REMOVE,
        CALL_MESSAGE_RECEIVED,
        CALL_UI,
        CONFERENCE_JOIN,
        CONFERENCE_LEAVE,
        CONFERENCE_STOP,
        CONFERENCE_QUERY,
        CONFERENCE_PARTP_JOIN,
        CONFERENCE_PARTP_LEAVE,
        CONFERENCE_PARTP_UPDATE,
        CONFERENCE_PROP_CHANGE,
        CONFERENCE_MESSAGE_RECEIVED,
        MESSAGE,
        STORAGE,
        GROUP_LIST,
        GROUP_INFO,
        ACCOUNT_QUERY_USER_STATUS
    }

    private EventType mEventType;

    public JCEvent(EventType eventType) {
        this.mEventType = eventType;
    }

    public EventType getEventType() {
        return mEventType;
    }

}

