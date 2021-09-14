package com.alan.mvvm.common.im.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;

import java.io.IOException;

/**
 * 作者：alan
 * 时间：2021/9/9
 * 备注：播放音频
 */
public class VoicePlayerUtil {

    private static VoicePlayerUtil instance = null;

    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private String playingId;

    private MediaPlayer.OnCompletionListener onCompletionListener;
    private final Context baseContext;

    public static VoicePlayerUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (VoicePlayerUtil.class) {
                if (instance == null) {
                    instance = new VoicePlayerUtil(context);
                }
            }
        }
        return instance;
    }

    private VoicePlayerUtil(Context cxt) {
        baseContext = cxt.getApplicationContext();
        audioManager = (AudioManager) baseContext.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
    }


    public MediaPlayer getPlayer() {
        return mediaPlayer;
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }


    public String getCurrentPlayingId() {
        return playingId;
    }

    public void play(final EMMessage msg, final MediaPlayer.OnCompletionListener listener) {
        if (!(msg.getBody() instanceof EMVoiceMessageBody)) return;

        if (mediaPlayer.isPlaying()) {
            stop();
        }

        playingId = msg.getMsgId();
        onCompletionListener = listener;

        try {
            setSpeaker();
            EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) msg.getBody();
            mediaPlayer.setDataSource(baseContext, voiceBody.getLocalUri());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop();

                    playingId = null;
                    onCompletionListener = null;
                }
            });
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        mediaPlayer.stop();
        mediaPlayer.reset();

        /**
         * 该监听器当前是停止语音播放动画，考虑以下3种情况：
         * 1.点击一个新的语音项播放，停止之前播放的语音项动画。
         * 2.语音播放完毕，停止语音播放动画。
         * 3.按录音键将停止语音播放，必须停止语音播放动画。
         *
         */
        if (onCompletionListener != null) {
            onCompletionListener.onCompletion(mediaPlayer);
        }
    }



    private void setSpeaker() {
        boolean speakerOn = false;
        if (speakerOn) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
    }
}
