package com.alan.module.easecallkit.widget;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alan.module.easecallkit.R;
import com.alan.module.easecallkit.activity.EaseVideoCallActivity;
import com.alan.mvvm.common.im.callkit.EaseCallKit;
import com.alan.mvvm.common.im.callkit.base.EaseCallType;
import com.alan.mvvm.common.im.callkit.base.EaseUserAccount;
import com.alan.mvvm.common.im.callkit.utils.EaseCallKitUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.EMLog;

import java.util.Map;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;


/**
 * 作者：alan
 * 时间：2021/9/16
 * 备注：
 */
public class EaseCallFloatWindow {
    private static final String TAG = "EaseCallFloatWindow";

    private Context context;

    private static EaseCallFloatWindow instance;

    private WindowManager windowManager = null;
    private WindowManager.LayoutParams layoutParams = null;

    private View floatView;
    private ImageView avatarView;
    private SurfaceView surfaceView;

    private int screenWidth;
    private int floatViewWidth;
    private EaseCallType callType;
    private EaseCallMemberView memberView;
    private RtcEngine rtcEngine;
    private int uId;
    private long costSeconds;
    private ConferenceInfo conferenceInfo;
    private SingleCallInfo singleCallInfo;
    private RelativeLayout ll_voice;
    private MyChronometer tvTime;

    public EaseCallFloatWindow(Context context) {
        initFloatWindow(context);
    }

    private EaseCallFloatWindow() {
    }


    public static EaseCallFloatWindow getInstance(Context context) {
        if (instance == null) {
            instance = new EaseCallFloatWindow(context);
        }
        return instance;
    }

    public static EaseCallFloatWindow getInstance() {
        if (instance == null) {
            synchronized (EaseCallFloatWindow.class) {
                if (instance == null) {
                    instance = new EaseCallFloatWindow();
                }
            }
        }
        return instance;
    }

    public void setCallType(EaseCallType callType) {
        this.callType = callType;
    }

    public EaseCallMemberView getCallMemberView() {
        return memberView;
    }

    public void setRtcEngine(RtcEngine rtcEngine) {
        this.rtcEngine = rtcEngine;
    }

    public void setRtcEngine(Context context, RtcEngine rtcEngine) {
        this.rtcEngine = rtcEngine;
        initFloatWindow(context);
    }

    private void initFloatWindow(Context context) {
        this.context = context;
        windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        screenWidth = point.x;
    }

    public EaseCallType getCallType() {
        return callType;
    }


    public void setCostSeconds(long seconds) {
        this.costSeconds = seconds;
    }

    /**
     * 添加悬浮窗
     * // 0: voice call; 1: video call;
     */
    public void show() {
        if (floatView != null) {
            return;
        }
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.END | Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.format = PixelFormat.TRANSPARENT;
        layoutParams.type = EaseCallKitUtils.getSupportedWindowType();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;

        floatView = LayoutInflater.from(context).inflate(R.layout.activity_float_window, null);
        floatView.setFocusableInTouchMode(true);
        floatView.setFocusable(true);
        floatView.requestFocus();
        windowManager.addView(floatView, layoutParams);


        avatarView = (ImageView) floatView.findViewById(R.id.iv_avatar);
        ll_voice = floatView.findViewById(R.id.layout_call_voice);
        TextView tvCalling = floatView.findViewById(R.id.tv_calling);
        ConstraintLayout clTime = floatView.findViewById(R.id.cl_time);
        tvTime = floatView.findViewById(R.id.tv_time);

        startCount();

        if (callType == EaseCallType.CONFERENCE_CALL) {
            conferenceInfo = new ConferenceInfo();
        } else {
            singleCallInfo = new SingleCallInfo();
        }


        floatView.post(new Runnable() {
            @Override
            public void run() {
                if (floatView != null) {
                    floatViewWidth = floatView.getWidth();
                }
            }
        });

        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EaseVideoCallActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                if (callType != EaseCallType.CONFERENCE_CALL) {
                    intent.putExtra("uId", singleCallInfo != null ? singleCallInfo.remoteUid : 0);
                }
                intent.putExtra("isClickByFloat", true);
                EaseCallKit.getInstance().getAppContext().startActivity(intent);
            }
        });

        floatView.setOnTouchListener(new View.OnTouchListener() {
            boolean result = false;

            int left;
            int top;
            float startX = 0;
            float startY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        result = false;
                        startX = event.getRawX();
                        startY = event.getRawY();

                        left = layoutParams.x;
                        top = layoutParams.y;

                        EMLog.i(TAG, "startX: " + startX + ", startY: " + startY + ", left: " + left + ", top: " + top);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(event.getRawX() - startX) > 20 || Math.abs(event.getRawY() - startY) > 20) {
                            result = true;
                        }

                        int deltaX = (int) (startX - event.getRawX());

                        layoutParams.x = left + deltaX;
                        layoutParams.y = (int) (top + event.getRawY() - startY);
                        EMLog.i(TAG, "startX: " + (event.getRawX() - startX) + ", startY: " + (event.getRawY() - startY)
                                + ", left: " + left + ", top: " + top);
                        windowManager.updateViewLayout(floatView, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        smoothScrollToBorder();
                        break;
                }
                return result;
            }
        });
    }

    private void startCount() {
        if (tvTime != null) {
            tvTime.setBase(SystemClock.elapsedRealtime());
            tvTime.start();
        }
    }

    private void stopCount() {
        if (tvTime != null) {
            tvTime.stop();
        }
    }

    /**
     * Should call the method before call {@link #dismiss()}
     *
     * @return Cost seconds in float window
     */
    public long getFloatCostSeconds() {
        if (tvTime != null) {
            return tvTime.getCostSeconds();
        }
        Log.e(TAG, "tvTime is null, can not get cost seconds");
        return 0;
    }

    /**
     * Should call the method before call {@link #dismiss()}
     *
     * @return Total cost seconds
     */
    public long getTotalCostSeconds() {
        if (tvTime != null) {
            Log.e("activity", "costSeconds: " + tvTime.getCostSeconds());
        }
        if (tvTime != null) {
            return costSeconds + tvTime.getCostSeconds();
        }
        Log.e(TAG, "tvTime is null, can not get total cost seconds");
        return 0;
    }

    public void setConferenceInfo(ConferenceInfo info) {
        this.conferenceInfo = info;
    }

    public ConferenceInfo getConferenceInfo() {
        return conferenceInfo;
    }

    /**
     * 更新电话会议状态
     *
     * @param view
     */
    public void update(EaseCallMemberView view) {
        if (floatView == null) {
            return;
        }
        memberView = view;
        uId = memberView.getUserId();
        if (memberView.isVideoOff()) { // 视频未开启
            ll_voice.setVisibility(View.VISIBLE);
            floatView.findViewById(R.id.layout_call_video).setVisibility(View.GONE);
        } else { // 视频已开启
            ll_voice.setVisibility(View.GONE);
            floatView.findViewById(R.id.layout_call_video).setVisibility(View.VISIBLE);

            String userAccount = memberView.getUserAccount();
            int uId = memberView.getUserId();
            boolean isSelf = TextUtils.equals(userAccount, EMClient.getInstance().getCurrentUser());
            prepareSurfaceView(isSelf, uId);
        }
    }

    /**
     * 更新单呼叫状态
     *
     * @param isSelf
     * @param curUid
     * @param remoteUid
     * @param surface
     */
    public void update(boolean isSelf, int curUid, int remoteUid, boolean surface) {
        if (singleCallInfo == null) {
            singleCallInfo = new SingleCallInfo();
        }
        singleCallInfo.curUid = curUid;
        singleCallInfo.remoteUid = remoteUid;
        if (callType == EaseCallType.SINGLE_VIDEO_CALL && surface) {
            ll_voice.setVisibility(View.GONE);
            floatView.findViewById(R.id.layout_call_video).setVisibility(View.VISIBLE);
            prepareSurfaceView(isSelf, isSelf ? curUid : remoteUid);
        } else {
            ll_voice.setVisibility(View.VISIBLE);
            floatView.findViewById(R.id.layout_call_video).setVisibility(View.GONE);
        }
    }

    public SingleCallInfo getSingleCallInfo() {
        return singleCallInfo;
    }

    public void setCameraDirection(boolean isFront, boolean changeFlag) {
        if (singleCallInfo == null) {
            singleCallInfo = new SingleCallInfo();
        }
        singleCallInfo.isCameraFront = isFront;
        singleCallInfo.changeFlag = changeFlag;
    }

    public boolean isShowing() {
        if (callType == EaseCallType.CONFERENCE_CALL) {
            return memberView != null;
        } else {
            return floatView != null;
        }
    }

    /**
     * For the single call, only the remote uid is returned
     *
     * @return
     */
    public int getUid() {
        if (callType == EaseCallType.CONFERENCE_CALL && memberView != null) {
            return memberView.getUserId();
        } else if ((callType == EaseCallType.SINGLE_VIDEO_CALL || callType == EaseCallType.SINGLE_VOICE_CALL) && singleCallInfo != null) {
            return singleCallInfo.remoteUid;
        }
        return -1;
    }

    /**
     * 停止悬浮窗
     */
    public void dismiss() {
        Log.i(TAG, "dismiss: ");
        if (windowManager != null && floatView != null) {
            stopCount();
            windowManager.removeView(floatView);
        }
        floatView = null;
        memberView = null;
        surfaceView = null;
        if (conferenceInfo != null) {
            conferenceInfo = null;
        }
        if (singleCallInfo != null) {
            singleCallInfo = null;
        }
    }

    /**
     * set call surface view
     */
    private void prepareSurfaceView(boolean isSelf, int uid) {
        RelativeLayout surfaceLayout = (RelativeLayout) floatView.findViewById(R.id.layout_call_video);
        surfaceLayout.removeAllViews();
        surfaceView =
                RtcEngine.CreateRendererView(EaseCallKit.getInstance().getAppContext());
        surfaceLayout.addView(surfaceView);
        surfaceView.setZOrderOnTop(false);
        surfaceView.setZOrderMediaOverlay(false);
        if (isSelf) {
            rtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        } else {
            rtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        }
    }

    private void smoothScrollToBorder() {
        EMLog.i(TAG, "screenWidth: " + screenWidth + ", floatViewWidth: " + floatViewWidth);
        int splitLine = screenWidth / 2 - floatViewWidth / 2;
        final int left = layoutParams.x;
        final int top = layoutParams.y;
        int targetX;

        if (left < splitLine) {
            // 滑动到最左边
            targetX = 0;
            ll_voice.setBackgroundResource(R.drawable.shape_float_left);
        } else {
            ll_voice.setBackgroundResource(R.drawable.shape_float_right);
            // 滑动到最右边
            targetX = screenWidth - floatViewWidth;
        }

        ValueAnimator animator = ValueAnimator.ofInt(left, targetX);
        animator.setDuration(100)
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (floatView == null) return;

                        int value = (int) animation.getAnimatedValue();
                        EMLog.i(TAG, "onAnimationUpdate, value: " + value);
                        layoutParams.x = value;
                        layoutParams.y = top;
                        windowManager.updateViewLayout(floatView, layoutParams);
                    }
                });
        animator.start();
    }

    public static class SingleCallInfo {
        /**
         * Current user's uid
         */
        public int curUid;
        /**
         * The other size of uid
         */
        public int remoteUid;
        /**
         * Camera direction: front or back
         */
        public boolean isCameraFront = true;
        /**
         * A tag used to mark the switch between local and remote video
         */
        public boolean changeFlag;
    }

    /**
     * Use to hold the conference info
     */
    public static class ConferenceInfo {
        public Map<Integer, ViewState> uidToViewList;
        public Map<String, Integer> userAccountToUidMap;
        public Map<Integer, EaseUserAccount> uidToUserAccountMap;

        /**
         * Hold the states of {@link EaseCallMemberView}
         */
        public static class ViewState {
            // video state
            public boolean isVideoOff;
            // audio state
            public boolean isAudioOff;
            // screen mode
            public boolean isFullScreenMode;
            // speak activate state
            public boolean speakActivated;
            // camera direction
            public boolean isCameraFront;
        }
    }
}
