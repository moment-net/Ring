package com.alan.mvvm.common.im.callkit.base;


public enum EaseCallType {
    SINGLE_VOICE_CALL(0), //1v1语音通话
    SINGLE_VIDEO_CALL(1); //1v1视频通话

    public int code;

    EaseCallType(int code) {
        this.code = code;
    }

    public static EaseCallType getfrom(int code) {
        switch (code) {
            case 0:
                return SINGLE_VOICE_CALL;
            case 1:
                return SINGLE_VIDEO_CALL;
            default:
                return SINGLE_VIDEO_CALL;
        }
    }
};