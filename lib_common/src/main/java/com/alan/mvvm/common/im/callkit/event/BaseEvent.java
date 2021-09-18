package com.alan.mvvm.common.im.callkit.event;

import com.alan.mvvm.common.im.callkit.base.EaseCallAction;


public class BaseEvent {
    public BaseEvent() {
    }

    public EaseCallAction callAction;
    public String callerDevId;
    public String calleeDevId;
    public long timeStramp;
    public String callId;
    public String msgType;
    public String userId;
}
