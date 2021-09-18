package com.alan.mvvm.common.im.callkit.event;

import com.alan.mvvm.common.im.callkit.base.EaseCallAction;


public class AnswerEvent extends BaseEvent {
    public AnswerEvent() {
        callAction = EaseCallAction.CALL_ANSWER;
    }

    public String result;
    public boolean transVoice;
}
