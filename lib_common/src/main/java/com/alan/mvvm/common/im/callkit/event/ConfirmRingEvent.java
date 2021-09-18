package com.alan.mvvm.common.im.callkit.event;

import com.alan.mvvm.common.im.callkit.base.EaseCallAction;


public class ConfirmRingEvent extends BaseEvent {
    public ConfirmRingEvent() {
        callAction = EaseCallAction.CALL_CONFIRM_RING;
    }

    public Boolean valid;
}
