package com.alan.module.chat.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.viewModels
import com.alan.module.chat.databinding.FragmentSpeakBinding
import com.alan.module.chat.dialog.DecorationFragmentDialog
import com.alan.module.chat.viewmodol.SpeakViewModel
import com.alan.mvvm.base.BaseApplication
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.ChatBgBean
import com.alan.mvvm.base.http.responsebean.SpeakVoiceBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.common.event.DecorationEvent
import com.alan.mvvm.common.event.EMMsgEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.im.utils.VoicePlayerUtil
import com.alan.mvvm.common.ui.BaseFragment
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import com.socks.library.KLog
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：
 */
@EventBusRegister
@AndroidEntryPoint
class SpeakFragment : BaseFragment<FragmentSpeakBinding, SpeakViewModel>() {

    override val mViewModel by viewModels<SpeakViewModel>()
    var chatBgBean: ChatBgBean? = null
    var userId = ""
    lateinit var voicePlayerUtil: VoicePlayerUtil
    var voiceList: LinkedList<SpeakVoiceBean> = LinkedList<SpeakVoiceBean>()

    companion object {
        @JvmStatic
        fun newInstance(userId: String) = SpeakFragment().apply {
            arguments = Bundle().apply {
                putString("userId", userId)
            }
        }
    }

    override fun FragmentSpeakBinding.initView() {
        arguments?.apply {
            userId = getString("userId")!!
        }

        mBinding.tvDecoration.clickDelay {
            if (chatBgBean != null) {
                val dialog = DecorationFragmentDialog.newInstance(userId)
                dialog.show(requireActivity().supportFragmentManager)
            }
        }

        voicePlayerUtil = VoicePlayerUtil.getInstance(BaseApplication.mContext)
    }

    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is ChatBgBean -> {
                    chatBgBean = it
                    CoilUtils.load(mBinding.ivBg, it.url)
                }

                is SpeakVoiceBean -> {
                    //语音
                    addVoiceList(it)
                }
            }
        }

        val userInfo = SpHelper.getUserInfo()
        CoilUtils.load(mBinding.ivSelf, userInfo?.model?.url!!)
    }

    override fun initRequestData() {
        mViewModel.requestChatBg(userId)
    }

    fun updateUserInfo(userInfoBean: UserInfoBean) {
        if (userInfoBean.model != null) {
            CoilUtils.load(mBinding.ivOther, userInfoBean.model?.url!!)
        }
    }

    //收到消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateChatBg(event: DecorationEvent) {
//        mViewModel.requestChatBg()
        chatBgBean = event.bean
        CoilUtils.load(mBinding.ivBg, chatBgBean?.url!!)
    }

    //收到消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun playTextToVoice(event: EMMsgEvent) {
        KLog.e("xujm", "准备播放消息")
        val msg = event.msg
        if (msg.type == EMMessage.Type.TXT) {
            val txtBody = msg.getBody() as EMTextMessageBody
            mViewModel.requestVoiceTTS(txtBody.message, msg)
        }
    }


    fun addVoiceList(voiceBean: SpeakVoiceBean) {
        voiceList.add(voiceBean)
        if (voiceList.size == 1) {
            startPlay()
        }
    }


    fun startPlay() {
        if (voiceList.size <= 0) return
        val voiceBean = voiceList.removeFirst()
        val audio = voiceBean.audio
        val msg = voiceBean.msg
        KLog.e("xujm", "当前播放音频：$audio")

        if (voicePlayerUtil.isPlaying) {
            //无论播放的语音项是这个还是其他，都先停止语音播放
            voicePlayerUtil.stop()
            // 停止语音播放动画。
            stopVoicePlayAnimation()

            // 如果正在播放的语音项是此项，则只需停止播放即可。
            val playingUrl: String = voicePlayerUtil.url
            if (TextUtils.equals(audio, playingUrl)) {
                return
            }
        }
        if (!TextUtils.isEmpty(audio)) {
            voicePlayerUtil.play(audio, MediaPlayer.OnCompletionListener {
                KLog.e("xujm", "开始播放声音")
                stopVoicePlayAnimation()
                startPlay()
            })
            // 启动语音播放动画
            startVoicePlayAnimation(msg)
        }
    }

    fun startVoicePlayAnimation(msg: EMMessage) {
        if (msg.direct() == EMMessage.Direct.RECEIVE) {
            mBinding.clSelf.gone()
            mBinding.clOther.visible()
            val txtBody = msg.getBody() as EMTextMessageBody
            mBinding.tvOther.setText(txtBody.message)
        } else {
            mBinding.clSelf.visible()
            mBinding.clOther.gone()
            val txtBody = msg.getBody() as EMTextMessageBody
            mBinding.tvOther.setText(txtBody.message)
        }
    }

    fun stopVoicePlayAnimation() {
        mBinding.clSelf.gone()
        mBinding.clOther.gone()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (voicePlayerUtil.isPlaying()) {
            voicePlayerUtil.stop()
        }
    }
}