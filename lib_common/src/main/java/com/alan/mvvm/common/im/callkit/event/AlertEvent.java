package com.alan.mvvm.common.im.callkit.event;

import com.alan.mvvm.common.im.callkit.utils.EaseCallAction;


public class AlertEvent extends BaseEvent {
    public AlertEvent() {
        callAction = EaseCallAction.CALL_ALERT;
    }
}