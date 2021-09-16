package com.alan.mvvm.common.im.callkit.event;

import com.alan.mvvm.common.im.callkit.utils.EaseCallAction;


public class CallCancelEvent extends BaseEvent {
    public CallCancelEvent() {
        callAction = EaseCallAction.CALL_CANCEL;
    }

    public boolean cancel = true;
    public boolean remoteTimeout = false;
}
