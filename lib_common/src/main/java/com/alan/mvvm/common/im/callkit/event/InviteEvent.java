package com.alan.mvvm.common.im.callkit.event;

import com.alan.mvvm.common.im.callkit.base.EaseCallType;
import com.alan.mvvm.common.im.callkit.utils.EaseCallAction;


public class InviteEvent extends BaseEvent {
    public InviteEvent() {
        callAction = EaseCallAction.CALL_INVITE;
    }

    public EaseCallType type;
}
