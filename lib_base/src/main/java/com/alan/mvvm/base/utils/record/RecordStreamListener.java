package com.alan.mvvm.base.utils.record;

/**
 * 作者：alan
 * 时间：2021/12/27
 * 备注：获取录音的音频流,用于拓展的处理
 */
public interface RecordStreamListener {
    void recordOfByte(byte[] data, int begin, int end);
}
