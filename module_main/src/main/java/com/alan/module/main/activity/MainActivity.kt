package com.alan.module.main.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.alan.module.home.dialog.CallFragmentDialog
import com.alan.module.main.R
import com.alan.module.main.adapter.MainVPAdapter
import com.alan.module.main.databinding.ActivityMainBinding
import com.alan.module.main.fragment.ChatFragment
import com.alan.module.main.fragment.HomeFragment
import com.alan.module.main.fragment.MyFragment
import com.alan.module.main.viewmodel.MainViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.alan.mvvm.base.utils.ActivityStackManager
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.IMConstant
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.db.entity.UserEntity
import com.alan.mvvm.common.event.CallEvent
import com.alan.mvvm.common.event.CallServiceEvent
import com.alan.mvvm.common.event.MessageEvent
import com.alan.mvvm.common.event.TokenEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.im.callkit.EaseCallKit
import com.alan.mvvm.common.im.callkit.base.EaseCallState
import com.alan.mvvm.common.im.push.HMSPushHelper
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.hyphenate.chat.EMMessage
import com.socks.library.KLog
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：首页
 */
@EventBusRegister
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_MAIN)
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<MainViewModel>()
    private val mFragments = arrayListOf<Fragment>()
    private var mPagePosition = 0


    /**
     * 初始化View
     */
    override fun ActivityMainBinding.initView() {
        initFragment()
        rbHome.clickDelay {
            mPagePosition = 0
            setPageState(mPagePosition)
        }
        rbChat.clickDelay {
            mPagePosition = 1
            setPageState(mPagePosition)
        }
        rbMy.clickDelay {
            mPagePosition = 2
            setPageState(mPagePosition)
        }
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {

    }

    /**
     * 获取数据
     */
    override fun initRequestData() {
        checkUnreadMsg(MessageEvent(IMConstant.EVENT_TYPE_MESSAGE, IMConstant.EVENT_EVENT_CHANGE))

        // 获取华为 HMS 推送 token
        HMSPushHelper.getInstance().getHMSToken(this)


        //判断是否为来电推送
        if (IMConstant.isRtcCall) {
            val bundle = Bundle().apply {}
            jumpARoute(
                RouteUrl.CallModule.ACTIVITY_CALL_CALL,
                bundle,
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
            IMConstant.isRtcCall = false
        }
    }


    /**
     * 初始化fragment
     */
    private fun initFragment() {
        if (mFragments.isNotEmpty()) {
            mFragments.clear()
        }
        mFragments.add(HomeFragment.newInstance())
        mFragments.add(ChatFragment.newInstance())
        mFragments.add(MyFragment.newInstance())
//        mFragments.add(SquareFragment.newInstance())

        mBinding.vpMain.adapter = MainVPAdapter(mFragments, supportFragmentManager, lifecycle)
        mBinding.vpMain.isUserInputEnabled = false
        mBinding.vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setButtonState(position)
            }
        })
    }

    fun setPageState(position: Int) {
        mBinding.vpMain.setCurrentItem(position, false)
    }

    fun setButtonState(position: Int) {
        mPagePosition = position
        when (position) {
            0 -> {
                mBinding.rgMain.check(R.id.rb_home)
            }
            1 -> {
                mBinding.rgMain.check(R.id.rb_chat)
            }
            2 -> {
                mBinding.rgMain.check(R.id.rb_my)
            }
        }
    }

    //获取新消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun checkUnreadMsg(event: MessageEvent) {
        when (event.type) {
            IMConstant.EVENT_TYPE_MESSAGE -> {
                handleConversations()
            }

            IMConstant.EVENT_TYPE_CONNECTION -> {
                mViewModel.loginIM()
            }
        }
    }

    //处理会话未读消息
    fun handleConversations() {
        val list = mViewModel.requestConversations()
        var unReadMsg: Int = 0
        for (conversation in list) {
            if (conversation.conversationId().length >= 16) {
                unReadMsg += conversation.unreadMsgCount
                if (conversation.allMsgCount > 0) {
                    for (msg in conversation.allMessages) {
                        if (msg.direct() == EMMessage.Direct.RECEIVE) {
                            val userId = conversation.lastMessage.from
                            val userName = conversation.lastMessage.getStringAttribute(
                                IMConstant.MESSAGE_ATTR_USERNAME,
                                ""
                            )
                            val avatar = conversation.lastMessage.getStringAttribute(
                                IMConstant.MESSAGE_ATTR_AVATAR,
                                ""
                            )
                            EMClientHelper.saveUser(UserEntity(userId, userName, avatar))
                            break
                        }
                    }
                }
            }
        }

        if (unReadMsg > 0) {
            mBinding.tvNum.visible()
            mBinding.tvNum.setText("$unReadMsg")
        } else {
            mBinding.tvNum.gone()
        }
    }

    //Token失效
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleToken(event: TokenEvent) {
        SpHelper.clearUserInfo()
        ActivityStackManager.finishAllActivity()
        val bundle = Bundle()
        jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_LOGIN, bundle, Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    //获取新消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showCall(event: CallEvent) {
        KLog.e("RingIM", "收到邀请弹起弹框")
        val dialog =
            CallFragmentDialog.newInstance(event.isComingCall, event.channelName, event.username)
        dialog.show((ActivityStackManager.getCurrentActivity() as FragmentActivity).supportFragmentManager)
    }

    //收到服务器加入和挂断消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleMsg(event: CallServiceEvent) {
        if (event.type == 1) {
            //加入
            val sessionId = EaseCallKit.getInstance().getSessionId()
            if (!TextUtils.isEmpty(sessionId)) {
                mViewModel.requestChatStart(sessionId)
            }
        } else {
            //挂断
            val sessionId = EaseCallKit.getInstance().getSessionId()
            if (!TextUtils.isEmpty(sessionId)) {
                mViewModel.requestChatHangup(sessionId)
            }
            //重置状态
            EaseCallKit.getInstance().callState = EaseCallState.CALL_IDLE
            EaseCallKit.getInstance().callID = null
            EaseCallKit.getInstance().setSessionId(null)
        }
    }


    override fun onResume() {
        super.onResume()
        EMClientHelper.showNotificationPermissionDialog()
    }


}