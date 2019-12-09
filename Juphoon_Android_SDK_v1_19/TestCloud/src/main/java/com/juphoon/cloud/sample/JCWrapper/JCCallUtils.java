package com.juphoon.cloud.sample.JCWrapper;

import com.juphoon.cloud.JCCall;
import com.juphoon.cloud.JCCallItem;

import java.util.Locale;

public class JCCallUtils {

    public static JCCallItem getActiveCall() {
        for (JCCallItem item : JCManager.getInstance().call.getCallItems()) {
            if (item.getActive()) {
                return item;
            }
        }
        return null;
    }

    public static boolean isIdle() {
        return JCManager.getInstance().call.getCallItems().size() == 0;
    }

    public static JCCallItem getNeedAnswerNotActiveCall() {
        for (JCCallItem item : JCManager.getInstance().call.getCallItems()) {
            if (!item.getActive() && item.getDirection() == JCCall.DIRECTION_IN && item.getState() == JCCall.STATE_PENDING) {
                return item;
            }
        }
        return null;
    }

    public static JCCallItem getIncomingCall() {
        for (JCCallItem item : JCManager.getInstance().call.getCallItems()) {
            if (item.getDirection() == JCCall.DIRECTION_IN && item.getState() == JCCall.STATE_PENDING) {
                return item;
            }
        }
        return null;
    }

    public static String genCallInfo(JCCallItem item) {
        if (item == null) {
            return "";
        }
        switch (item.getState()) {
            case JCCall.STATE_INIT:
                return "呼叫中";
            case JCCall.STATE_PENDING:
                return "振铃中";
            case JCCall.STATE_CONNECTING:
                return "连接中";
            case JCCall.STATE_TALKING:
                if (item.getHold()) {
                    return "挂起";
                } else if (item.getHeld()) {
                    return "被挂起";
                } else if (item.getOtherAudioInterrupt()) {
                    return "对方声音中断";
                } else {
                    long secondes = System.currentTimeMillis() / 1000 - item.getTalkingBeginTime();
                    return String.format(Locale.getDefault(), "%02d:%02d", secondes / 60, secondes % 60);
                }
            case JCCall.STATE_OK:
                return "通话结束";
            case JCCall.STATE_CANCEL:
                return "通话结束";
            case JCCall.STATE_CANCELED:
                return "挂断";
            case JCCall.STATE_MISSED:
                return "未接";
            default:
                return "异常";
        }
    }

    public static String genNetStatus(JCCallItem item) {
        if (item.getState() != JCCall.STATE_TALKING) {
            return "";
        }
        switch (item.getNetStatus()) {
            case JCCall.NET_STATUS_DISCONNECTED:
                return "无网络";
            case JCCall.NET_STATUS_VERY_BAD:
                return "很差";
            case JCCall.NET_STATUS_BAD:
                return "差";
            case JCCall.NET_STATUS_NORMAL:
                return "一般";
            case JCCall.NET_STATUS_GOOD:
                return "好";
            case JCCall.NET_STATUS_VERY_GOOD:
                return "非常好";
            default:
                return "";
        }
    }

}
