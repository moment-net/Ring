package com.alan.mvvm.common.im.callkit.event;

import com.alan.mvvm.common.im.callkit.base.EaseCallAction;
import com.alan.mvvm.common.im.callkit.base.EaseCallType;


public class InviteEvent extends BaseEvent {
    public InviteEvent() {
        callAction = EaseCallAction.CALL_INVITE;
    }

    public EaseCallType type;
}
