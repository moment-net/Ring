package com.alan.mvvm.base.utils;

import android.util.Base64;

import com.socks.library.KLog;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 作者：alan
 * 时间：2021/11/22
 * 备注：AES加解密
 */
public class AESUtils {


    private static final String TAG = AESUtils.class.getSimpleName();

    /**
     * 采用AES加密算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 字符编码(用哪个都可以，要注意new String()默认使用UTF-8编码 getBytes()默认使用ISO8859-1编码)
     */
    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static final String SECRETKEY = "c91ebrurpkblmnah";

    /**
     * AES 加密
     * <p>
     * 加密密码，长度：16 或 32 个字符
     *
     * @param data 待加密内容
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String data) {
        try {
            // 创建AES秘钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRETKEY.getBytes(CHARSET_UTF8), KEY_ALGORITHM);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 初始化加密器
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptByte = cipher.doFinal(data.getBytes(CHARSET_UTF8));
            // 将加密以后的数据进行 Base64 编码
            return base64Encode(encryptByte);
        } catch (Exception e) {
            handleException("encrypt", e);
        }
        return null;
    }

    /**
     * AES 解密
     * <p>
     * 解密的密钥，长度：16 或 32 个字符
     *
     * @param base64Data 加密的密文 Base64 字符串
     */
    public static String decrypt(String base64Data) {
        try {
            byte[] data = base64Decode(base64Data);
            // 创建AES秘钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRETKEY.getBytes(CHARSET_UTF8), KEY_ALGORITHM);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // 初始化解密器
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            // 执行解密操作
            byte[] result = cipher.doFinal(data);
            return new String(result, CHARSET_UTF8);
        } catch (Exception e) {
            handleException("decrypt", e);
        }
        return null;
    }

    /**
     * 将 字节数组 转换成 Base64 编码
     * 用Base64.DEFAULT模式会导致加密的text下面多一行（在应用中显示是这样）
     */
    public static String base64Encode(byte[] data) {
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    /**
     * 将 Base64 字符串 解码成 字节数组
     */
    public static byte[] base64Decode(String data) {
        return Base64.decode(data, Base64.NO_WRAP);
    }

    /**
     * 处理异常
     */
    private static void handleException(String methodName, Exception e) {
        e.printStackTrace();
        KLog.e(TAG, methodName + "---->" + e);
    }


}
