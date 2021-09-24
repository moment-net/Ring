package com.alan.module.easecallkit.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alan.module.easecallkit.R;
import com.alan.module.easecallkit.activity.CallActivity;
import com.alan.mvvm.base.coil.CoilUtils;
import com.alan.mvvm.common.im.callkit.utils.EaseCallKitUtils;
import com.socks.library.KLog;


/**
 * 作者：alan
 * 时间：2021/9/16
 * 备注：
 */
public class EaseCommingCallView extends FrameLayout {

    private static final String TAG = CallActivity.class.getSimpleName();

    private ImageButton mBtnReject;
    private ImageButton mBtnPickup;
    private TextView mInviterName;
    private OnActionListener mOnActionListener;
    private EaseImageView avatar_view;
    private Bitmap headBitMap;
    private String headUrl;

    public EaseCommingCallView(@NonNull Context context) {
        this(context, null);
    }

    public EaseCommingCallView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EaseCommingCallView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.activity_comming_call, this);
        mBtnReject = findViewById(R.id.btn_reject);
        mBtnPickup = findViewById(R.id.btn_pickup);
        mInviterName = findViewById(R.id.tv_nick);
        avatar_view = findViewById(R.id.iv_avatar);
        mBtnReject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnActionListener != null) {
                    mOnActionListener.onRejectClick(v);
                }
            }
        });

        mBtnPickup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnActionListener != null) {
                    mOnActionListener.onPickupClick(v);
                }
            }
        });
    }

    public void setInviteInfo(String username) {
        mInviterName.setText(EaseCallKitUtils.getUserNickName(username));
        headUrl = EaseCallKitUtils.getUserHeadImage(username);

        //加载头像图片
        loadHeadImage();
    }

    /**
     * 加载用户配置头像
     *
     * @return
     */
    private void loadHeadImage() {
        if (headUrl != null) {
            if (headUrl.startsWith("http://") || headUrl.startsWith("https://")) {
                CoilUtils.INSTANCE.loadCircle(avatar_view, headUrl);
            } else {
                if (headBitMap == null) {
                    //该方法直接传文件路径的字符串，即可将指定路径的图片读取到Bitmap对象
                    headBitMap = BitmapFactory.decodeFile(headUrl);
                }
                if (headBitMap != null && !headBitMap.isRecycled()) {
                    avatar_view.setImageBitmap(headBitMap);
                } else {
                    KLog.e(TAG, "headBitMap is isRecycled");
                }
            }
        }
    }


    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }


    public void setOnActionListener(OnActionListener listener) {
        this.mOnActionListener = listener;
    }

    public interface OnActionListener {
        void onRejectClick(View v);

        void onPickupClick(View v);
    }


    float[] getScreenInfo(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        float[] info = new float[5];
        if (manager != null) {
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            info[0] = dm.widthPixels;
            info[1] = dm.heightPixels;
            info[2] = dm.densityDpi;
            info[3] = dm.density;
            info[4] = dm.scaledDensity;
        }
        return info;
    }
}

