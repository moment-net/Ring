package com.alan.mvvm.common.im.callkit.base;


/**
 * 作者：alan
 * 时间：2021/9/16
 * 备注：
 */
public enum EaseCallAction {
    //邀请通话
    CALL_INVITE("invite"),
    //会话验证消息
    CALL_ALERT("alert"),
    //会话确认消息
    CALL_CONFIRM_RING("confirmRing"),
    //取消通话
    CALL_CANCEL("cancelCall"),
    //接受通话
    CALL_ANSWER("answerCall"),
    CALL_CONFIRM_CALLEE("confirmCallee"),
    //视频转语音
    CALL_VIDEO_TO_VOICE("videoToVoice");

    public String state;

    EaseCallAction(String state) {
        this.state = state;
    }

    public static EaseCallAction getfrom(String state) {
        switch (state) {
            case "invite":
                return CALL_INVITE;
            case "alert":
                return CALL_ALERT;
            case "confirmRing":
                return CALL_CONFIRM_RING;
            case "cancelCall":
                return CALL_CANCEL;
            case "answerCall":
                return CALL_ANSWER;
            case "confirmCallee":
                return CALL_CONFIRM_CALLEE;
            case "videoToVoice":
                return CALL_VIDEO_TO_VOICE;
            default:
                return CALL_INVITE;
        }
    }
}
