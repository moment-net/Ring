package com.alan.module.main.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.alan.module.main.R
import com.alan.module.main.adapter.MainVPAdapter
import com.alan.module.main.databinding.FragmentHomeBinding
import com.alan.module.main.dialog.PushFragmentDialog
import com.alan.module.main.dialog.StateFragmentDialog
import com.alan.module.main.viewmodel.HomeViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.ChangeThinkEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：测试fragment
 */
@EventBusRegister
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply {
            arguments.apply { }
        }
    }

    private val mFragments = arrayListOf<Fragment>()
    override val mViewModel by viewModels<HomeViewModel>()



    override fun FragmentHomeBinding.initView() {
        ivAvatar.clickDelay {
//            val dialog = MatchFragmentDialog.newInstance(SpHelper.getUserInfo()?.userId!!)
//            dialog.show(requireActivity().supportFragmentManager)
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MY)
        }
        tvState.clickDelay {
            val dialog = StateFragmentDialog.newInstance()
            dialog.show(requireActivity().supportFragmentManager)
        }
        tvNow.clickDelay {
            changeTab(0)
            viewpager.setCurrentItem(0, true)
        }
        tvThink.clickDelay {
            changeTab(1)
            viewpager.setCurrentItem(1, true)
        }
        ivAdd.clickDelay {
            val dialog = PushFragmentDialog.newInstance()
            dialog.show(requireActivity().supportFragmentManager)
        }

        initFragment()
    }

    override fun initObserve() {

    }

    override fun initRequestData() {

    }

    fun changeTab(position: Int) {
        if (position == 0) {
            mBinding.tvNow.setTextColor(R.color._3A3A3A.getResColor())
            mBinding.tvNow.textSize = 20f
            mBinding.tvThink.setTextColor(R.color._803A3A3A.getResColor())
            mBinding.tvThink.textSize = 14f
        } else {
            mBinding.tvNow.setTextColor(R.color._803A3A3A.getResColor())
            mBinding.tvNow.textSize = 14f
            mBinding.tvThink.setTextColor(R.color._3A3A3A.getResColor())
            mBinding.tvThink.textSize = 20f
        }
    }

    /**
     * 初始化fragment
     */
    private fun initFragment() {
        if (mFragments.isNotEmpty()) {
            mFragments.clear()
        }
        mFragments.add(NowFragment.newInstance())
        mFragments.add(ThinkFragment.newInstance())

        mBinding.viewpager.adapter =
            MainVPAdapter(mFragments, requireActivity().supportFragmentManager, lifecycle)
        mBinding.viewpager.isUserInputEnabled = false
        mBinding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                changeTab(position)
            }
        })
    }


    override fun onResume() {
        super.onResume()
        setUserInfo()
    }

    fun setUserInfo() {
        val userInfoBean = SpHelper.getUserInfo()
        CoilUtils.loadRoundBorder(
            mBinding.ivAvatar,
            userInfoBean?.avatar!!,
            17f,
            1f,
            R.color.white.getResColor()
        )
        mBinding.tvName.setText(userInfoBean.userName)
    }

    //切换
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showCall(event: ChangeThinkEvent) {
        mBinding.viewpager.setCurrentItem(1)
    }

}
