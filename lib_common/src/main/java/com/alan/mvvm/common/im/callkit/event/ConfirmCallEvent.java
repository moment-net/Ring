package com.alan.mvvm.common.im.callkit.event;

import com.alan.mvvm.common.im.callkit.base.EaseCallAction;


public class ConfirmCallEvent extends BaseEvent {
    public ConfirmCallEvent() {
        callAction = EaseCallAction.CALL_CONFIRM_CALLEE;
    }

    public String result;
}
