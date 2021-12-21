package com.alan.module.main.dialog

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alan.module.main.R
import com.alan.module.main.databinding.LayoutDialogShareBinding
import com.alan.module.main.viewmodel.CallDialogViewModel
import com.alan.module.main.views.ShareView
import com.alan.mvvm.base.http.responsebean.SoundResultBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.BitmapUtil
import com.alan.mvvm.common.constant.Constants
import com.socks.library.KLog
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ShareFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogShareBinding, CallDialogViewModel>() {
    // IWXAPI 是第三方app和微信通信的openApi接口
    private var api: IWXAPI? = null
    var bitmap: Bitmap? = null

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<CallDialogViewModel>()
    var bean: SoundResultBean? = null

    companion object {
        fun newInstance(bean: SoundResultBean): ShareFragmentDialog {
            val bundle = Bundle()
            bundle.putParcelable("bean", bean)
            val dialog = ShareFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
        }
    }


    override fun initWindow() {
        super.initWindow()
        val window = dialog!!.window
        val params = window!!.attributes
        val colorDrawable = ColorDrawable(
            ContextCompat.getColor(
                mActivity, R.color.transparent
            )
        )
        window.setBackgroundDrawable(colorDrawable)
        params.width = dp2px(360f)
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        params.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(true)
        isCancelable = true
        window.attributes = params
    }

    override fun LayoutDialogShareBinding.initView() {
        arguments?.apply {
            bean = getParcelable<SoundResultBean>("bean")
        }
        tvWx.clickDelay {
            shareWX()
        }
        tvPyq.clickDelay {
            shareWXTimeline()
        }
        tvCancel.clickDelay {
            dismiss()
        }

        val shareView = ShareView(requireActivity())
        shareView.setInfo(bean)

        lifecycleScope.launch {
            flow<Int> {
                delay(1000)
                emit(1)
            }.collect {
//                toast("图片保存成功,快去分享吧～")
                bitmap = shareView.createImage()

//                //插入到系统图库
                val path = BitmapUtil.save(requireActivity(), bitmap)
                val uri = Uri.fromFile(File(path))
                //发送广播通知系统图库刷新数据
                requireActivity().sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
                KLog.e("xujm", "path:$path")
            }
        }
    }


    override fun initObserve() {

    }

    override fun initRequestData() {
        regToWx()
    }

    //注册微信
    private fun regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(activity, Constants.APP_ID, true)
    }

    /**
     * 分享到微信
     */
    fun shareWX() {
        //初始化 WXImageObject 和 WXMediaMessage 对象
        val imgObj = WXImageObject(bitmap)
        val msg = WXMediaMessage()
        msg.mediaObject = imgObj

        //设置缩略图
        val thumbBmp = Bitmap.createScaledBitmap(bitmap!!, 270, 125, true)
        msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true)

        //构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = SendMessageToWX.Req.WXSceneSession
        req.userOpenId = Constants.APP_ID
        //调用api接口，发送数据到微信
        api!!.sendReq(req)
    }

    private fun buildTransaction(type: String?): String? {
        return if (type == null) System.currentTimeMillis()
            .toString() else type + System.currentTimeMillis()
    }


    /**
     * 分享到微信朋友圈
     */
    fun shareWXTimeline() {
        //初始化 WXImageObject 和 WXMediaMessage 对象
        val imgObj = WXImageObject(bitmap)
        val msg = WXMediaMessage()
        msg.mediaObject = imgObj

        //设置缩略图
        val thumbBmp = Bitmap.createScaledBitmap(bitmap!!, 270, 125, true)
        msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true)

        //构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = SendMessageToWX.Req.WXSceneTimeline
        req.userOpenId = Constants.APP_ID
        //调用api接口，发送数据到微信
        api!!.sendReq(req)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bitmap != null && !bitmap!!.isRecycled) {
            bitmap!!.recycle()
        }
    }
}