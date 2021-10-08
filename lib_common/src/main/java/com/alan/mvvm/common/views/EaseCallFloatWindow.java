package com.alan.mvvm.common.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alan.mvvm.base.utils.UtilsKt;
import com.alan.mvvm.common.R;
import com.alan.mvvm.common.constant.RouteUrl;
import com.alan.mvvm.common.im.callkit.EaseCallKit;
import com.alan.mvvm.common.im.callkit.base.EaseCallState;
import com.alan.mvvm.common.im.callkit.base.EaseCallType;
import com.alan.mvvm.common.im.callkit.utils.EaseCallKitUtils;
import com.socks.library.KLog;


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
    private int screenWidth;
    private int floatViewWidth;
    private EaseCallType callType;
    private long costSeconds;
    private RelativeLayout ll_voice;
    private MyChronometer tvTime;
    private ConstraintLayout clTime;
    private TextView tvCalling;
    private String username;
    private String channelName;
    private boolean isComingCall;


    public static EaseCallFloatWindow getInstance(Context context) {
        if (instance == null) {
            synchronized (EaseCallFloatWindow.class) {
                if (instance == null) {
                    instance = new EaseCallFloatWindow(context);
                }
            }
        }
        return instance;
    }

    public EaseCallFloatWindow(Context context) {
        initFloatWindow(context);
    }


    private void initFloatWindow(Context context) {
        this.context = context;
        windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        screenWidth = point.x;
    }


    public void setCallType(EaseCallType callType) {
        this.callType = callType;
    }

    public EaseCallType getCallType() {
        return callType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public boolean isComingCall() {
        return isComingCall;
    }

    public void setComingCall(boolean comingCall) {
        isComingCall = comingCall;
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

        floatView = LayoutInflater.from(context).inflate(R.layout.layout_float_window, null);
        floatView.setFocusableInTouchMode(true);
        floatView.setFocusable(true);
        floatView.requestFocus();
        windowManager.addView(floatView, layoutParams);


        ll_voice = floatView.findViewById(R.id.layout_call_voice);
        tvCalling = floatView.findViewById(R.id.tv_calling);
        clTime = floatView.findViewById(R.id.cl_time);
        tvTime = floatView.findViewById(R.id.tv_time);


        startCount();



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
                Bundle bundle = new Bundle();
                bundle.putBoolean("isClickByFloat", true);
                if (!TextUtils.isEmpty(username)) {
                    bundle.putBoolean("isComingCall", isComingCall);
                    bundle.putString("channelName", channelName);
                    bundle.putString("username", username);
                }
                UtilsKt.jumpARoute(RouteUrl.CallModule.ACTIVITY_CALL_CALL, bundle);
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

                        KLog.e(TAG, "startX: " + startX + ", startY: " + startY + ", left: " + left + ", top: " + top);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(event.getRawX() - startX) > 20 || Math.abs(event.getRawY() - startY) > 20) {
                            result = true;
                        }

                        int deltaX = (int) (startX - event.getRawX());

                        layoutParams.x = left + deltaX;
                        layoutParams.y = (int) (top + event.getRawY() - startY);
                        KLog.e(TAG, "startX: " + (event.getRawX() - startX) + ", startY: " + (event.getRawY() - startY)
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
            tvTime.setBase(SystemClock.elapsedRealtime() - costSeconds * 1000);
            tvTime.start();
        }
    }

    private void stopCount() {
        if (tvTime != null) {
            tvTime.stop();
        }
    }

    /**
     * 应该在调用之前调用方法{@link #dismiss()}
     * 获取秒数
     *
     * @return 总秒数
     */
    public long getTotalCostSeconds() {
        if (tvTime != null) {
            Log.e("activity", "costSeconds: " + tvTime.getCostSeconds());
        }
        if (tvTime != null) {
            return tvTime.getCostSeconds();
        }
        Log.e(TAG, "tvTime is null, can not get total cost seconds");
        return 0;
    }


    /**
     * 更新单呼叫状态
     */
    public void updateState() {
        if (isShowing()) {
            EaseCallState state = EaseCallKit.getInstance().getCallState();
            if (state == EaseCallState.CALL_ANSWERED) {
                tvCalling.setVisibility(View.GONE);
                clTime.setVisibility(View.VISIBLE);
                startCount();
            } else {
                tvCalling.setVisibility(View.VISIBLE);
                clTime.setVisibility(View.GONE);
            }
        }
    }


    public boolean isShowing() {
        return floatView != null;
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

        username = null;
        channelName = null;
        isComingCall = false;
    }


    private void smoothScrollToBorder() {
        KLog.e(TAG, "screenWidth: " + screenWidth + ", floatViewWidth: " + floatViewWidth);
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
                        KLog.e(TAG, "onAnimationUpdate, value: " + value);
                        layoutParams.x = value;
                        layoutParams.y = top;
                        windowManager.updateViewLayout(floatView, layoutParams);
                    }
                });
        animator.start();
    }


}
