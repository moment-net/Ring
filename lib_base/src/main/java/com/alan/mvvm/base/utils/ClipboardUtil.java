package com.alan.mvvm.base.utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClipboardUtil {

    /**
     * 复制文本到剪贴板
     *
     * @param context 上下文
     * @param text    文本
     */
    public static void copyText(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_PLAIN, text));
    }

    /**
     * 清空剪贴板内容
     */
    public static void clearClipboard(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clipboard.getPrimaryClip());
            clipboard.setPrimaryClip(null);
        }
    }


    /**
     * 获取剪贴板的文本
     *
     * @param context 上下文
     * @return 剪贴板的文本
     */
    public static String getText(final Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //无数据时直接返回
        if (!clipboard.hasPrimaryClip()) {
            return "";
        }
        //如果是文本信息
        if (clipboard.getPrimaryClipDescription() != null) {
            if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                ClipData cdText = clipboard.getPrimaryClip();
                if (cdText != null) {
                    ClipData.Item item = cdText.getItemAt(0);
                    //此处是TEXT文本信息
                    if (item.getText() != null) {
                        String str = item.getText().toString();
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int i = 0; i < str.length(); i++) {
                            char c = str.charAt(i);
                            if (!isEmojiCharacter(c)) {
                                stringBuffer.append(c);
                            }
                        }
                        if (stringBuffer.length() == 24) {
                            return stringBuffer.toString();
                        }
                    }
                }
            }
        }
        return "";
    }

    /**
     * 正则判断emoji表情
     *
     * @param input
     * @return
     */
    private static boolean isEmoji(String input) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\ud83e\udc00-\ud83e\udfff]" +
                "|[\u2100-\u32ff]|[\u0030-\u007f][\u20d0-\u20ff]|[\u0080-\u00ff]");
        Matcher m = p.matcher(input);
        return m.find();
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }
}

