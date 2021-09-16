package com.alan.module.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
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
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.IMConstant
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.MessageEvent
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.im.callkit.base.EaseCallType
import com.alan.mvvm.common.im.push.HMSPushHelper
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
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
            if (EaseCallType.getfrom(IMConstant.type) != EaseCallType.CONFERENCE_CALL) {
                val bundle = Bundle().apply {}
                jumpARoute(
                    RouteUrl.CallModule.ACTIVITY_CALL_CALL,
                    bundle,
                    Intent.FLAG_ACTIVITY_NEW_TASK
                )
            } else {
                val bundle = Bundle().apply {}
                jumpARoute(
                    RouteUrl.CallModule.ACTIVITY_CALL_CALLS,
                    bundle,
                    Intent.FLAG_ACTIVITY_NEW_TASK
                )
            }
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
                val count = EMClientHelper.chatManager.unreadMessageCount
                if (count > 0) {
                    mBinding.tvNum.visible()
                    mBinding.tvNum.setText("$count")
                } else {
                    mBinding.tvNum.gone()
                }
            }
            IMConstant.EVENT_TYPE_CONNECTION -> {
                mViewModel.loginIM()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        EMClientHelper.showNotificationPermissionDialog()
    }


}