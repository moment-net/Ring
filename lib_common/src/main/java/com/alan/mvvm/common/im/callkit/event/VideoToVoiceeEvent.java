package com.alan.mvvm.common.im.callkit.event;

import com.alan.mvvm.common.im.callkit.utils.EaseCallAction;


public class VideoToVoiceeEvent extends BaseEvent {
    public VideoToVoiceeEvent() {
        callAction = EaseCallAction.CALL_VIDEO_TO_VOICE;
    }
}
