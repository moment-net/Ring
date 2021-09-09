package com.alan.module.chat.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alan.module.chat.R;
import com.alan.module.im.utils.VoicePlayerUtil;
import com.alan.module.im.utils.VoiceRecorderUtil;
import com.alan.mvvm.base.utils.StorageUtil;
import com.hyphenate.EMError;

/**
 * 作者：alan
 * 时间：2021/9/9
 * 备注：录音机视图
 */
public class VoiceRecorderView extends RelativeLayout {
    protected Context context;
    protected Drawable[] micImages = new Drawable[]{getResources().getDrawable(R.drawable.ease_record_animate_01),
            getResources().getDrawable(R.drawable.ease_record_animate_02),
            getResources().getDrawable(R.drawable.ease_record_animate_03),
            getResources().getDrawable(R.drawable.ease_record_animate_04),
            getResources().getDrawable(R.drawable.ease_record_animate_05),
            getResources().getDrawable(R.drawable.ease_record_animate_06),
            getResources().getDrawable(R.drawable.ease_record_animate_07),
            getResources().getDrawable(R.drawable.ease_record_animate_08),
            getResources().getDrawable(R.drawable.ease_record_animate_09),
            getResources().getDrawable(R.drawable.ease_record_animate_10),
            getResources().getDrawable(R.drawable.ease_record_animate_11),
            getResources().getDrawable(R.drawable.ease_record_animate_12),
            getResources().getDrawable(R.drawable.ease_record_animate_13),
            getResources().getDrawable(R.drawable.ease_record_animate_14),};

    protected VoiceRecorderUtil voiceRecorder;

    protected ImageView iv_mic;
    protected TextView tv_recording_hint;

    protected Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            int index = msg.what;
            if (index < 0 || index > micImages.length - 1) {
                return;
            }
            iv_mic.setImageDrawable(micImages[index]);
        }
    };

    public VoiceRecorderView(Context context) {
        super(context);
        init(context);
    }

    public VoiceRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoiceRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_voice_recorder, this);

        iv_mic = (ImageView) findViewById(R.id.iv_mic);
        tv_recording_hint = (TextView) findViewById(R.id.tv_recording_hint);

        voiceRecorder = new VoiceRecorderUtil(micImageHandler);
    }

    /**
     * on speak button touched
     *
     * @param v
     * @param event
     */
    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event, EaseVoiceRecorderCallback recorderCallback) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    VoicePlayerUtil voicePlayer = VoicePlayerUtil.getInstance(context);
                    if (voicePlayer.isPlaying())
                        voicePlayer.stop();
                    v.setPressed(true);
                    setTextContent(v, true);
                    startRecording();
                } catch (Exception e) {
                    v.setPressed(false);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                setTextContent(v, true);
                if (event.getY() < 0) {
                    showReleaseToCancelHint();
                } else {
                    showMoveUpToCancelHint();
                }
                return true;
            case MotionEvent.ACTION_UP:
                v.setPressed(false);
                setTextContent(v, false);
                if (event.getY() < 0) {
                    // discard the recorded audio.
                    discardRecording();
                } else {
                    // stop recording and send voice file
                    try {
                        int length = stopRecoding();
                        if (length > 0) {
                            if (recorderCallback != null) {
                                recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                            }
                        } else if (length == EMError.FILE_INVALID) {
                            Toast.makeText(context, "无录音权限", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "说话时间太短", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "发送失败，请检测服务器是否连接", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            default:
                discardRecording();
                return false;
        }
    }

    private void setTextContent(View view, boolean pressed) {
        if (view instanceof ViewGroup && ((ViewGroup) view).getChildCount() > 0) {
            View child = ((ViewGroup) view).getChildAt(0);
            if (child instanceof TextView) {
                ((TextView) child).setText(pressed ? "松开发送" : "按住说话");
            }
        }
    }

    public interface EaseVoiceRecorderCallback {
        /**
         * on voice record complete
         *
         * @param voiceFilePath   录音完毕后的文件路径
         * @param voiceTimeLength 录音时长
         */
        void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);
    }

    public void startRecording() {
        if (!StorageUtil.isExternalStorageWritable()) {
            Toast.makeText(context, "发送语音需要sdcard支持", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            this.setVisibility(View.VISIBLE);
            tv_recording_hint.setText("手指上滑，取消发送");
            tv_recording_hint.setBackgroundColor(Color.TRANSPARENT);
            voiceRecorder.startRecording(context);
        } catch (Exception e) {
            e.printStackTrace();
            if (voiceRecorder != null)
                voiceRecorder.discardRecording();
            this.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "录音失败，请重试！", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void showReleaseToCancelHint() {
        tv_recording_hint.setText("松开手指，取消发送");
        tv_recording_hint.setBackgroundColor(Color.TRANSPARENT);
    }

    public void showMoveUpToCancelHint() {
        tv_recording_hint.setText("手指上滑，取消发送");
        tv_recording_hint.setBackgroundColor(Color.TRANSPARENT);
    }

    public void discardRecording() {

        try {
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                this.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    public int stopRecoding() {
        this.setVisibility(View.INVISIBLE);

        return voiceRecorder.stopRecoding();
    }

    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }


    public boolean isRecording() {
        return voiceRecorder.isRecording();
    }

}
