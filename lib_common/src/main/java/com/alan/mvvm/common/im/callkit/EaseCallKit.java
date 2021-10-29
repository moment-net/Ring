package com.alan.mvvm.common.im.callkit;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alan.mvvm.base.utils.EventBusUtils;
import com.alan.mvvm.base.utils.UtilsKt;
import com.alan.mvvm.common.constant.IMConstant;
import com.alan.mvvm.common.constant.RouteUrl;
import com.alan.mvvm.common.event.CallDismissEvent;
import com.alan.mvvm.common.event.CallEvent;
import com.alan.mvvm.common.event.CallHangupEvent;
import com.alan.mvvm.common.event.CallServiceEvent;
import com.alan.mvvm.common.event.MatchEvent;
import com.alan.mvvm.common.im.callkit.base.EaseCallAction;
import com.alan.mvvm.common.im.callkit.base.EaseCallInfo;
import com.alan.mvvm.common.im.callkit.base.EaseCallKitConfig;
import com.alan.mvvm.common.im.callkit.base.EaseCallKitListener;
import com.alan.mvvm.common.im.callkit.base.EaseCallState;
import com.alan.mvvm.common.im.callkit.base.EaseCallType;
import com.alan.mvvm.common.im.callkit.base.EaseMsgUtils;
import com.alan.mvvm.common.im.callkit.event.AlertEvent;
import com.alan.mvvm.common.im.callkit.event.AnswerEvent;
import com.alan.mvvm.common.im.callkit.event.BaseEvent;
import com.alan.mvvm.common.im.callkit.event.CallCancelEvent;
import com.alan.mvvm.common.im.callkit.event.ConfirmCallEvent;
import com.alan.mvvm.common.im.callkit.event.InviteEvent;
import com.alan.mvvm.common.im.callkit.livedatas.EaseLiveDataBus;
import com.alan.mvvm.common.im.callkit.utils.EaseCallKitNotifier;
import com.alan.mvvm.common.im.callkit.utils.EaseCallKitUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EasyUtils;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


/**
 * 作者：alan
 * 时间：2021/9/16
 * 备注：该工具包是帮助开发人员使用 CallKit 的帮助类，它提供了启动音频和视频的方法
 */
public class EaseCallKit {
    private static final String TAG = "RingIM";
    private static EaseCallKit instance = null;
    private boolean callKitInit = false;
    private Context appContext = null;
    private EMMessageListener messageListener = null;
    private EaseCallType callType = EaseCallType.SINGLE_VIDEO_CALL;
    private EaseCallState callState = EaseCallState.CALL_IDLE;
    private String channelName;
    private String fromUserId; //被叫获取主叫的
    public static String deviceId = "android_";
    public String clallee_devId;
    private String callID = null;
    private JSONObject inviteExt = null;
    private EaseCallInfo callInfo = new EaseCallInfo();
    private TimeHandler timeHandler;
    private Map<String, EaseCallInfo> callInfoMap = new HashMap<>();
    private EaseCallKitListener callListener;
    private static boolean isComingCall = true;
    private ArrayList<String> inviteeUsers = new ArrayList<>();
    private EaseCallKitConfig callKitConfig;
    private EaseCallKitNotifier notifier;
    public String sessionId;//音频会话id;发起聊天和挂断聊天使用

    private EaseCallKit() {
    }

    public static EaseCallKit getInstance() {
        if (instance == null) {
            synchronized (EaseCallKit.class) {
                if (instance == null) {
                    instance = new EaseCallKit();
                }
            }
        }
        return instance;
    }

    /**
     * init 初始化
     *
     * @param context
     * @return
     */
    public synchronized boolean init(Context context, EaseCallKitConfig config) {
        if (callKitInit) {
            return true;
        }
        removeMessageListener();
        appContext = context;
        if (!isMainProcess(appContext)) {
            Log.e(TAG, "enter the service process!");
            return false;
        }

        //获取设备序列号
        deviceId += EaseCallKitUtils.getPhoneSign();
        timeHandler = new TimeHandler();

        //设置callkit配置项
        callKitConfig = new EaseCallKitConfig();
        callKitConfig.setAgoraAppId(config.getAgoraAppId());
        callKitConfig.setUserInfoMap(config.getUserInfoMap());
        callKitConfig.setDefaultHeadImage(config.getDefaultHeadImage());
        callKitConfig.setCallTimeOut(config.getCallTimeOut());
        callKitConfig.setRingFile(config.getRingFile());
        callKitConfig.setEnableRTCToken(config.isEnableRTCToken());

        //init notifier
        initNotifier();

        //增加接收消息回调
        addMessageListener();
        callKitInit = true;
        return true;
    }


    /**
     * 获取当前callKitConfig
     */
    public EaseCallKitConfig getCallKitConfig() {
        return callKitConfig;
    }


    private void initNotifier() {
        notifier = new EaseCallKitNotifier(appContext);
    }


    /**
     * 通话错误类型
     */
    public enum EaseCallError {
        PROCESS_ERROR, //业务逻辑异常
        RTC_ERROR, //音视频异常
        IM_ERROR  //IM异常
    }

    public enum CALL_PROCESS_ERROR {
        CALL_STATE_ERROR(0),
        CALL_TYPE_ERROR(1),
        CALL_PARAM_ERROR(2),
        CALL_RECEIVE_ERROR(3);

        public int code;

        CALL_PROCESS_ERROR(int code) {
            this.code = code;
        }
    }


    /**
     * 加入1v1通话
     * 注意：在相关activity结束时需要调用，防止出现内存泄漏
     *
     * @param type 通话类型(只能为SINGLE_VOICE_CALL或SINGLE_VIDEO_CALL类型）
     * @param user 被叫用户ID(也就是环信ID)
     * @param ext  扩展字段(用户扩展字段)
     */
    public void startSingleCall(final EaseCallType type, final String user, final Map<String, Object> ext) {
        if (callState != EaseCallState.CALL_IDLE) {
            if (callListener != null) {
                callListener.onCallError(EaseCallError.PROCESS_ERROR, CALL_PROCESS_ERROR.CALL_STATE_ERROR.code, "current state is busy");
            }
            return;
        }
        if (user != null && user.length() == 0) {
            if (callListener != null) {
                callListener.onCallError(EaseCallError.PROCESS_ERROR, CALL_PROCESS_ERROR.CALL_PARAM_ERROR.code, "user is null");
            }
            return;
        }
        callType = type;
        //改为主动呼叫状态
        callState = EaseCallState.CALL_OUTGOING;
        fromUserId = user;
        if (ext != null) {
            inviteExt = EaseCallKitUtils.convertMapToJSONObject(ext);
        }

        //开始1V1通话
        Bundle bundle = new Bundle();
        isComingCall = false;
        bundle.putBoolean("isComingCall", false);
        bundle.putString("username", user);
        channelName = EaseCallKitUtils.getRandomString(10);
        bundle.putString("channelName", channelName);
        UtilsKt.jumpARoute(RouteUrl.CallModule.ACTIVITY_CALL_CALL, bundle, FLAG_ACTIVITY_NEW_TASK);
    }



    /**
     * 增加消息监听
     */
    private void addMessageListener() {
        this.messageListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    String messageType = message.getStringAttribute(EaseMsgUtils.CALL_MSG_TYPE, "");
                    KLog.e(TAG, "收到消息:" + message.getMsgId() + " from:" + message.getFrom() + "  messageType:" + messageType);
                    //有关通话控制信令
                    if (TextUtils.equals(messageType, EaseMsgUtils.CALL_MSG_INFO)
                            && !TextUtils.equals(message.getFrom(), EMClient.getInstance().getCurrentUser())) {
                        String action = message.getStringAttribute(EaseMsgUtils.CALL_ACTION, "");
                        String callerDevId = message.getStringAttribute(EaseMsgUtils.CALL_DEVICE_ID, "");
                        String fromCallId = message.getStringAttribute(EaseMsgUtils.CLL_ID, "");
                        String fromUser = message.getFrom();
                        String channel = message.getStringAttribute(EaseMsgUtils.CALL_CHANNELNAME, "");
                        JSONObject ext = null;
                        try {
                            ext = message.getJSONObjectAttribute(EaseMsgUtils.CALL_INVITE_EXT);
                        } catch (HyphenateException exception) {
                            exception.printStackTrace();
                        }

                        if (action == null || callerDevId == null || fromCallId == null || fromUser == null || channel == null) {
                            if (callListener != null) {
                                callListener.onCallError(EaseCallError.PROCESS_ERROR, CALL_PROCESS_ERROR.CALL_RECEIVE_ERROR.code, "receive message error");
                            }
                            continue;
                        }
                        EaseCallAction callAction = EaseCallAction.getfrom(action);
                        switch (callAction) {
                            case CALL_INVITE: //收到通话邀请
                                int calltype = message.getIntAttribute
                                        (EaseMsgUtils.CALL_TYPE, 0);
                                EaseCallType callkitType =
                                        EaseCallType.getfrom(calltype);
                                if (callState != EaseCallState.CALL_IDLE) {
                                    //收到邀请对话并且处于正在通话状态
                                    if (TextUtils.equals(fromCallId, callID) && TextUtils.equals(fromUser, fromUserId)
                                            && callkitType == EaseCallType.SINGLE_VOICE_CALL && callType == EaseCallType.SINGLE_VIDEO_CALL) {
                                        //同个语音通话-收到转音频事件
                                        InviteEvent inviteEvent = new InviteEvent();
                                        inviteEvent.callId = fromCallId;
                                        inviteEvent.type = callkitType;

                                        //发布消息
                                        EaseLiveDataBus.get().with(EaseCallType.SINGLE_VIDEO_CALL.toString()).postValue(inviteEvent);
                                    } else {
                                        //不同语音通话-发送忙碌状态
                                        AnswerEvent callEvent = new AnswerEvent();
                                        callEvent.result = EaseMsgUtils.CALL_ANSWER_BUSY;
                                        callEvent.callerDevId = callerDevId;
                                        callEvent.callId = fromCallId;
                                        sendCmdMsg(callEvent, fromUser);
                                    }
                                } else {
                                    //收到邀请对话并且处于闲置状态
                                    callInfo.setCallerDevId(callerDevId);
                                    callInfo.setCallId(fromCallId);
                                    callInfo.setCallKitType(callkitType);
                                    callInfo.setChannelName(channel);
                                    callInfo.setComming(true);
                                    callInfo.setFromUser(fromUser);
                                    callInfo.setExt(ext);

                                    //邀请信息放入列表中
                                    callInfoMap.put(fromCallId, callInfo);

                                    //发送alert消息
                                    AlertEvent callEvent = new AlertEvent();
                                    callEvent.callerDevId = callerDevId;
                                    callEvent.callId = fromCallId;
                                    sendCmdMsg(callEvent, fromUser);

                                    //启动定时器
                                    timeHandler.startTime();
                                }
                                break;
                            default:
                                break;
                        }

                    }


                    if (message.getType() == EMMessage.Type.TXT
                            && message.ext() != null
                            && message.ext().size() > 0) {
                        //自定义消息 (服务端下发的房间消息)
                        int command = Integer.parseInt(String.valueOf(message.ext().get(IMConstant.MESSAGE_KEY_COMMOND)));
                        String data = String.valueOf(message.ext().get(IMConstant.MESSAGE_KEY_DATA));
                        KLog.e(TAG, "服务器下发消息：command：" + command + " data:" + data);
                        if (command == IMConstant.MESSAGE_COMMOND_LAUNCH) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                String sessionId = jsonObject.optString("sessionId");
                                setSessionId(sessionId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (command == IMConstant.MESSAGE_COMMOND_JOINED) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                String sessionId = jsonObject.optString("sessionId");
                                setSessionId(sessionId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            EventBusUtils.INSTANCE.postEvent(new CallServiceEvent(1));
                        } else if (command == IMConstant.MESSAGE_COMMOND_HANGUP) {
                            EventBusUtils.INSTANCE.postEvent(new CallServiceEvent(2));
                            EventBusUtils.INSTANCE.postEvent(new CallHangupEvent(1));
                        } else if (command == IMConstant.MESSAGE_COMMOND_MATCH_SUCCESS) {
                            EventBusUtils.INSTANCE.postEvent(new MatchEvent(data));
                        }
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    String messageType = message.getStringAttribute(EaseMsgUtils.CALL_MSG_TYPE, "");
//                    KLog.e(TAG, "Receive cmdmsg:" + message.getMsgId() + " from:" + message.getFrom() + "  messageType:" + messageType);
                    //有关通话控制信令
                    if (TextUtils.equals(messageType, EaseMsgUtils.CALL_MSG_INFO)
                            && !TextUtils.equals(message.getFrom(), EMClient.getInstance().getCurrentUser())) {
                        String action = message.getStringAttribute(EaseMsgUtils.CALL_ACTION, "");
                        String callerDevId = message.getStringAttribute(EaseMsgUtils.CALL_DEVICE_ID, "");
                        String fromCallId = message.getStringAttribute(EaseMsgUtils.CLL_ID, "");
                        String fromUser = message.getFrom();
                        String channel = message.getStringAttribute(EaseMsgUtils.CALL_CHANNELNAME, "");
                        EaseCallAction callAction = EaseCallAction.getfrom(action);
                        KLog.e(TAG, "收到命令消息:" + message.getMsgId() + " from:" + message.getFrom() + "  messageType:" + messageType + "  callAction:" + callAction + "  callState:" + callState);
                        switch (callAction) {
                            case CALL_CANCEL: //取消通话
                                if (callState == EaseCallState.CALL_IDLE) {
                                    timeHandler.stopTime();
                                    //取消呼叫
                                    callInfoMap.remove(fromCallId);
                                } else {
                                    CallCancelEvent event = new CallCancelEvent();
                                    event.callerDevId = callerDevId;
                                    event.callId = fromCallId;
                                    event.userId = fromUser;
                                    if (TextUtils.equals(callID, fromCallId)) {
                                        callState = EaseCallState.CALL_IDLE;
                                    }
                                    notifier.reset();
                                    //发布消息
                                    EaseLiveDataBus.get().with(EaseCallType.SINGLE_VIDEO_CALL.toString()).postValue(event);
                                }
                                EventBusUtils.INSTANCE.postEvent(new CallDismissEvent());
                                break;
                            case CALL_ALERT:
                                String calleedDeviceId = message.getStringAttribute(EaseMsgUtils.CALLED_DEVICE_ID, "");
                                AlertEvent alertEvent = new AlertEvent();
                                alertEvent.callId = fromCallId;
                                alertEvent.calleeDevId = calleedDeviceId;
                                alertEvent.userId = fromUser;
                                EaseLiveDataBus.get().with(EaseCallType.SINGLE_VIDEO_CALL.toString()).postValue(alertEvent);
                                break;
                            case CALL_CONFIRM_RING: //收到callId 是否有效
                                String calledDvId = message.getStringAttribute(EaseMsgUtils.CALLED_DEVICE_ID, "");
                                boolean vaild = message.getBooleanAttribute(EaseMsgUtils.CALL_STATUS, false);
                                //多端设备时候用于区别哪个DrviceId,
                                // 被叫处理自己设备Id的CALL_CONFIRM_RING
                                if (TextUtils.equals(calledDvId, deviceId)) {
                                    EventBusUtils.INSTANCE.postEvent(new CallDismissEvent());
                                    timeHandler.stopTime();
                                    if (!vaild) {
                                        //通话无效
                                        callInfoMap.remove(fromCallId);
                                    } else {
                                        //收到callId 有效
                                        if (callState == EaseCallState.CALL_IDLE) {
                                            callState = EaseCallState.CALL_ALERTING;
                                            //对方主叫的设备信息
                                            clallee_devId = callerDevId;
                                            callID = fromCallId;
                                            EaseCallInfo info = callInfoMap.get(fromCallId);
                                            if (info != null) {
                                                channelName = info.getChannelName();
                                                callType = info.getCallKitType();
                                                fromUserId = info.getFromUser();
                                                inviteExt = info.getExt();
                                            }
                                            //收到有效的呼叫map邀请信息
                                            callInfoMap.clear();
                                            timeHandler.startSendEvent();
                                        } else {
                                            //通话无效
                                            callInfoMap.remove(fromCallId);
                                            timeHandler.stopTime();
                                        }
                                    }
                                }
                                break;
                            case CALL_CONFIRM_CALLEE:  //收到仲裁消息
                                String result = message.getStringAttribute(EaseMsgUtils.CALL_RESULT, "");
                                String calledDevId = message.getStringAttribute(EaseMsgUtils.CALLED_DEVICE_ID, "");
                                ConfirmCallEvent event = new ConfirmCallEvent();
                                event.calleeDevId = calledDevId;
                                event.result = result;
                                event.callerDevId = callerDevId;
                                event.callId = fromCallId;
                                event.userId = fromUser;
                                //发布消息
                                EaseLiveDataBus.get().with(EaseCallType.SINGLE_VIDEO_CALL.toString()).postValue(event);
                                break;
                            case CALL_ANSWER:
                                //收到被叫的回复消息
                                String result1 = message.getStringAttribute(EaseMsgUtils.CALL_RESULT, "");
                                String calledDevId1 = message.getStringAttribute(EaseMsgUtils.CALLED_DEVICE_ID, "");
                                boolean transVoice = message.getBooleanAttribute(EaseMsgUtils.CALLED_TRANSE_VOICE, false);
                                //判断不是被叫另外一台设备的漫游消息
                                //或者是主叫收到的
                                if (!isComingCall || TextUtils.equals(calledDevId1, deviceId)) {
                                    AnswerEvent answerEvent = new AnswerEvent();
                                    answerEvent.result = result1;
                                    answerEvent.calleeDevId = calledDevId1;
                                    answerEvent.callerDevId = callerDevId;
                                    answerEvent.callId = fromCallId;
                                    answerEvent.userId = fromUser;
                                    answerEvent.transVoice = transVoice;

                                    //发布消息
                                    EaseLiveDataBus.get().with(EaseCallType.SINGLE_VIDEO_CALL.toString()).postValue(answerEvent);
                                }
                                break;
                            case CALL_VIDEO_TO_VOICE:
                                if (callState != EaseCallState.CALL_IDLE) {
                                    if (TextUtils.equals(fromCallId, callID)
                                            && TextUtils.equals(fromUser, fromUserId)) {
                                        InviteEvent inviteEvent = new InviteEvent();
                                        inviteEvent.callId = fromCallId;
                                        inviteEvent.type = EaseCallType.SINGLE_VOICE_CALL;
                                        //发布消息
                                        EaseLiveDataBus.get().with(EaseCallType.SINGLE_VIDEO_CALL.toString()).postValue(inviteEvent);
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> messages) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };
        //增加消息监听
        EMClient.getInstance().chatManager().addMessageListener(this.messageListener);

    }

    /***
     * 设置call kit监听
     * @param listener
     * @return
     */
    public void setCallKitListener(EaseCallKitListener listener) {
        this.callListener = listener;
    }


    /***
     * 移除call kit监听
     * @param listener
     * @return
     */
    public void removeCallKitListener(EaseCallKitListener listener) {
        this.callListener = null;
    }


    /**
     * 移除消息监听
     */
    private void removeMessageListener() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        messageListener = null;
    }

    public EaseCallState getCallState() {
        return callState;
    }

    public EaseCallType getCallType() {
        return callType;
    }

    public void setCallType(EaseCallType callType) {
        this.callType = callType;
    }

    public void setCallState(EaseCallState callState) {
        this.callState = callState;
    }

    public String getCallID() {
        return callID;
    }

    public void setCallID(String callID) {
        this.callID = callID;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getClallee_devId() {
        return clallee_devId;
    }

    public EaseCallKitListener getCallListener() {
        return callListener;
    }


    private boolean isMainProcess(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return TextUtils.equals(context.getApplicationInfo().packageName, appProcess.processName);
            }
        }
        return false;
    }


    /**
     * 发送CMD回复信息
     *
     * @param username
     */
    public void sendCmdMsg(BaseEvent event, String username) {
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.CMD);
        message.setTo(username);
        String action = "rtcCall";
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        message.addBody(cmdBody);

        message.setAttribute(EaseMsgUtils.CALL_ACTION, event.callAction.state);
        message.setAttribute(EaseMsgUtils.CALL_DEVICE_ID, event.callerDevId);
        message.setAttribute(EaseMsgUtils.CLL_ID, event.callId);
        message.setAttribute(EaseMsgUtils.CLL_TIMESTRAMEP, System.currentTimeMillis());
        message.setAttribute(EaseMsgUtils.CALL_MSG_TYPE, EaseMsgUtils.CALL_MSG_INFO);
        if (event.callAction == EaseCallAction.CALL_ANSWER) {
            message.setAttribute(EaseMsgUtils.CALLED_DEVICE_ID, deviceId);
            message.setAttribute(EaseMsgUtils.CALL_RESULT, ((AnswerEvent) event).result);
        } else if (event.callAction == EaseCallAction.CALL_ALERT) {
            message.setAttribute(EaseMsgUtils.CALLED_DEVICE_ID, deviceId);
        }
        final EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username, EMConversation.EMConversationType.Chat, true);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                KLog.e(TAG, "Invite call success");
                conversation.removeMessage(message.getMsgId());
            }

            @Override
            public void onError(int code, String error) {
                KLog.e(TAG, "Invite call error " + code + ", " + error);
                conversation.removeMessage(message.getMsgId());
                if (callListener != null) {
                    callListener.onCallError(EaseCallError.IM_ERROR, code, error);
                }
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
        EMClient.getInstance().chatManager().sendMessage(message);
    }


    private class TimeHandler extends Handler {
        private final int MSG_TIMER = 0;
        private final int MSG_START_ACTIVITY = 1;
        private DateFormat dateFormat = null;
        private int timePassed = 0;

        public TimeHandler() {
            dateFormat = new SimpleDateFormat("HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        public void startTime() {
            timePassed = 0;
            sendEmptyMessageDelayed(MSG_TIMER, 1000);
        }

        public void startSendEvent() {
            sendEmptyMessage(MSG_START_ACTIVITY);
        }

        public void stopTime() {
            removeMessages(MSG_START_ACTIVITY);
            removeMessages(MSG_TIMER);
        }


        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_TIMER) {
                // TODO:更新通话时间
                timePassed++;
                String time = dateFormat.format(timePassed * 1000);
                if (timePassed * 1000 == EaseMsgUtils.CALL_INVITED_INTERVAL) {

                    //呼叫超时
                    timeHandler.stopTime();
                    callState = EaseCallState.CALL_IDLE;
                }
                sendEmptyMessageDelayed(MSG_TIMER, 1000);
            } else if (msg.what == MSG_START_ACTIVITY) {
                timeHandler.stopTime();
                String info = "";
                String userName = EaseCallKitUtils.getUserNickName(fromUserId);

                //启动activity
                isComingCall = true;
                KLog.e("RingIM", "发起语音聊天");
                EventBusUtils.INSTANCE.postStickyEvent(new CallEvent(true, channelName, fromUserId));

                //发送通知
                if (Build.VERSION.SDK_INT >= 29 && !EasyUtils.isAppRunningForeground(appContext)) {
                    KLog.e(TAG, "notify:" + info);
                    if (callType == EaseCallType.SINGLE_VIDEO_CALL) {
                        info = userName + "发起视频邀请";
                    } else {
                        info = userName + "发起语音邀请";
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("ring://com.moment.ring/main/"));
//                    notifier.notify(intent, "Ring", info);
                }

                //通话邀请回调
                if (callListener != null) {
                    callListener.onReceivedCall(callType, fromUserId, inviteExt);
                }
            }
            super.handleMessage(msg);
        }
    }



    public ArrayList<String> getInviteeUsers() {
        return inviteeUsers;
    }

    public void initInviteeUsers() {
        inviteeUsers.clear();
    }

    public JSONObject getInviteExt() {
        return inviteExt;
    }

    public Context getAppContext() {
        return appContext;
    }
}
