package com.alan.mvvm.base.utils

import android.content.Context
import com.alan.mvvm.base.http.responsebean.StsTokenBean
import com.alibaba.sdk.android.oss.*
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.luck.picture.lib.entity.LocalMedia
import com.socks.library.KLog
import java.util.*


/**
 * OSS图片上传的管理类
 */
class OssManager {
    private var mOSS: OSS? = null

    companion object {
        //通过@JvmStatic注解，使得在Java中调用instance直接是像调用静态函数一样，
        //AmplitudeUtil.getInstance(),如果不加注解，在Java中必须这样调用: KLazilyDCLSingleton.Companion.getInstance().
        //使用lazy属性代理，并指定LazyThreadSafetyMode为SYNCHRONIZED模式保证线程安全
        @JvmStatic
        val mInstance: OssManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            OssManager()
        }
    }


    /**
     * 创建OSS对象
     */
    private fun getOSS(context: Context, bean: StsTokenBean): OSS? {
        if (mOSS == null) {
//            val provider: OSSCredentialProvider = object : OSSCustomSignerCredentialProvider() {
//                override fun signContent(content: String): String {
//                    return OSSUtils.sign(bean.accessKeyId, bean.accessKeySecret, content)
//                }
//            }
            val provider: OSSCredentialProvider = OSSStsTokenCredentialProvider(
                bean.accessKeyId,
                bean.accessKeySecret,
                bean.securityToken
            )
            val conf = ClientConfiguration()
            conf.setConnectionTimeout(15 * 1000) // 连接超时，默认15秒
            conf.setSocketTimeout(15 * 1000) // socket超时，默认15秒
            conf.setMaxConcurrentRequest(5) // 最大并发请求书，默认5个
            conf.setMaxErrorRetry(2) // 失败后最大重试次数，默认2次
//            mOSS = OSSClient(context, bean.endPoint, provider, conf)
            mOSS = OSSClient(context, bean.endPoint, provider, conf)
        }
        return mOSS
    }

    /**
     * 图片上传
     *
     * @param context
     * @param uploadFilePath   图片的本地路径
     * @param onUploadListener 回调监听
     */
    fun upload(
        context: Context, bean: StsTokenBean, position: Int, item: LocalMedia,
        onUploadListener: OnUploadListener?
    ) {
        KLog.e("uploadPic", "FileName:${item.fileName}=====${item.realPath}")
        val oss = getOSS(context, bean)
        // 创建上传的对象
        val put = PutObjectRequest(
            bean.bucketName,
            "image/${getUUIDByRules32Image()}", item.realPath
        )
        // 上传的进度回调
        put.setProgressCallback(OSSProgressCallback<PutObjectRequest?> { request, currentSize, totalSize ->
            if (onUploadListener == null) {
                return@OSSProgressCallback
            }
            onUploadListener.onProgress(position, currentSize, totalSize)
        })

//            try {
//                val putResult = oss!!.putObject(put)
//                KLog.d("PutObject", "UploadSuccess")
//                KLog.d("ETag", putResult.eTag)
//                KLog.d("RequestId", putResult.requestId)
//            } catch (e: ClientException) {
//                // 客户端异常，例如网络异常等。
//                e.printStackTrace()
//            } catch (e: ServiceException) {
//                // 服务端异常。
//                KLog.e("RequestId", e.requestId)
//                KLog.e("ErrorCode", e.errorCode)
//                KLog.e("HostId", e.hostId)
//                KLog.e("RawMessage", e.rawMessage)
//            }
        oss?.asyncPutObject(
            put,
            object : OSSCompletedCallback<PutObjectRequest, PutObjectResult?> {
                override fun onSuccess(
                    request: PutObjectRequest,
                    result: PutObjectResult?
                ) {
                    KLog.e(
                        "uploadPic",
                        "上传成功：$position 是否是空:${onUploadListener == null} ${request.getObjectKey()}"
                    )
                    if (onUploadListener == null) {
                        return
                    }
                    val imageUrl: String = request.getObjectKey()
                    onUploadListener.onSuccess(
                        position, item,
                        bean.bucketDomain + imageUrl
                    )
                }

                override fun onFailure(
                    request: PutObjectRequest,
                    clientException: ClientException,
                    serviceException: ServiceException
                ) {
                    KLog.e("uploadPic", "上传失败：$position")
                    serviceException.printStackTrace()
                    clientException.printStackTrace()
                    if (onUploadListener == null) {
                        return
                    }
                    onUploadListener.onFailure(position)
                }
            })
    }

    interface OnUploadListener {
        /**
         * 上传的进度
         */
        fun onProgress(position: Int, currentSize: Long, totalSize: Long)

        /**
         * 成功上传
         */
        fun onSuccess(position: Int, item: LocalMedia, imageUrl: String?)

        /**
         * 上传失败
         */
        fun onFailure(position: Int)
    }

    /**
     * 上传到后台的图片的名称
     */
    fun getUUIDByRules32Image(): String {
        var generateRandStr: StringBuffer? = null
        try {
            val rules = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            var rpoint = 0
            generateRandStr = StringBuffer()
            val rand = Random()
            val length = 32
            for (i in 0 until length) {
                if (rules != null) {
                    rpoint = rules.length
                    val randNum: Int = rand.nextInt(rpoint)
                    generateRandStr.append(rules.substring(randNum, randNum + 1))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (generateRandStr == null) {
            "getUUIDByRules32Image.png"
        } else "$generateRandStr.png"
    }


}