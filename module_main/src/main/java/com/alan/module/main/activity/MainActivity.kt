package com.alan.module.main.activity

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
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：首页
 */
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
}