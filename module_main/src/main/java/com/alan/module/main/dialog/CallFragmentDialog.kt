package com.alan.module.home.dialog

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.LayoutCallBinding
import com.alan.module.main.viewmodel.CallDialogViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.CallServiceEvent
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.im.callkit.EaseCallKit
import com.alan.mvvm.common.im.callkit.base.EaseMsgUtils
import com.alan.mvvm.common.im.callkit.event.AnswerEvent
import com.alan.mvvm.common.views.EaseCallFloatWindow
import com.lzf.easyfloat.permission.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallFragmentDialog : BaseFrameDialogFragment<LayoutCallBinding, CallDialogViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<CallDialogViewModel>()


    companion object {
        fun newInstance(
            isComingCall: Boolean,
            channelName: String,
            username: String
        ): CallFragmentDialog {
            val bundle = Bundle()
            bundle.putBoolean("isComingCall", isComingCall)
            bundle.putString("channelName", channelName)
            bundle.putString("username", username)
            val dialog = CallFragmentDialog()
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
        params.width = dp2px(320f)
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.TOP
        setCanceledOnTouchOutside(true)
        isCancelable = true
        window.attributes = params
    }

    override fun LayoutCallBinding.initView() {
        val isComingCall = arguments?.getBoolean("isComingCall", true)
        val channelName = arguments?.getString("channelName", "")
        val userId = arguments?.getString("username", "")
        clBg.clickDelay {
            val bundle = Bundle()
            bundle.putBoolean("isComingCall", isComingCall!!)
            bundle.putString("channelName", channelName)
            bundle.putString("username", userId)
            jumpARoute(
                RouteUrl.CallModule.ACTIVITY_CALL_CALL,
                bundle,
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
            dismiss()
        }

        clFinish.setOnFinishListener {
            if (PermissionUtils.checkPermission(requireContext())) {
                EaseCallFloatWindow.getInstance(requireContext()).setCostSeconds(0)
                EaseCallFloatWindow.getInstance(requireContext()).show()
                EaseCallFloatWindow.getInstance(requireContext()).isComingCall = true
                EaseCallFloatWindow.getInstance(requireContext()).channelName = channelName
                EaseCallFloatWindow.getInstance(requireContext()).username = userId
                EaseCallFloatWindow.getInstance(requireContext()).update(true, 0, 0)
                EaseCallFloatWindow.getInstance(requireContext()).setCameraDirection(true, true)
            } else {
                val bundle = Bundle()
                bundle.putBoolean("isComingCall", isComingCall!!)
                bundle.putString("channelName", channelName)
                bundle.putString("username", userId)
                jumpARoute(
                    RouteUrl.CallModule.ACTIVITY_CALL_CALL,
                    bundle,
                    Intent.FLAG_ACTIVITY_NEW_TASK
                )
            }
            dismiss()
        }

        tvChat.clickDelay {
            val bundle = Bundle()
            bundle.putBoolean("isComingCall", isComingCall!!)
            bundle.putString("channelName", channelName)
            bundle.putString("username", userId)
            bundle.putBoolean("isDialogCall", true)
            jumpARoute(
                RouteUrl.CallModule.ACTIVITY_CALL_CALL,
                bundle,
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
            dismiss()
        }
        tvCancel.clickDelay {
            //发送拒绝消息
            val event = AnswerEvent()
            event.result = EaseMsgUtils.CALL_ANSWER_REFUSE
            event.callId = EaseCallKit.getInstance().callID
            event.callerDevId = EaseCallKit.getInstance().getClallee_devId()
            event.calleeDevId = EaseCallKit.deviceId
            EaseCallKit.getInstance().sendCmdMsg(event, userId)


            EventBusUtils.postEvent(CallServiceEvent(2))
            dismiss()
        }

        val userEntity = EMClientHelper.getUserById(userId!!)
        tvName.setText("${userEntity.userName}正在邀请你边吃边聊")
        CoilUtils.loadRoundBorder(ivAvatar, userEntity.avatar, 12f, 1f, R.color.white.getResColor())
    }


    override fun initObserve() {
    }

    override fun initRequestData() {
    }


}