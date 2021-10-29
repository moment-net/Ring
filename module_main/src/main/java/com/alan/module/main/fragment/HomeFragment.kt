package com.alan.module.main.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.alan.module.home.dialog.MatchingFragmentDialog
import com.alan.module.main.R
import com.alan.module.main.adapter.HomeMatchAdapter
import com.alan.module.main.adapter.TabAdapter
import com.alan.module.main.databinding.FragmentHomeBinding
import com.alan.module.main.dialog.FilterFragmentDialog
import com.alan.module.main.dialog.PushFragmentDialog
import com.alan.module.main.viewmodel.HomeViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.http.responsebean.NowTagBean
import com.alan.mvvm.base.http.responsebean.TabItemBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.db.entity.UserEntity
import com.alan.mvvm.common.dialog.DialogHelper
import com.alan.mvvm.common.event.ChangeThinkEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.report.DataPointUtil
import com.alan.mvvm.common.ui.BaseFragment
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.bigkoo.convenientbanner.listener.OnItemClickListener
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
    lateinit var mAdapter: HomeMatchAdapter


    @RequiresApi(Build.VERSION_CODES.M)
    override fun FragmentHomeBinding.initView() {
        ivAvatar.clickDelay {
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MY)
            DataPointUtil.reportHomeMy(SpHelper.getUserInfo()?.userId!!)
        }
        ivAvatarHide.clickDelay {
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MY)
            DataPointUtil.reportHomeMy(SpHelper.getUserInfo()?.userId!!)
        }
        ivFilter.clickDelay {
//            val dialog = StateFragmentDialog.newInstance()
//            dialog.show(requireActivity().supportFragmentManager)
            val dialog = FilterFragmentDialog.newInstance()
            dialog.show(requireActivity().supportFragmentManager)
        }
        ivFilterHide.clickDelay {
//            val dialog = StateFragmentDialog.newInstance()
//            dialog.show(requireActivity().supportFragmentManager)
            val dialog = FilterFragmentDialog.newInstance()
            dialog.show(requireActivity().supportFragmentManager)
        }
        clMatch.clickDelay {
            val dialog = MatchingFragmentDialog.newInstance(SpHelper.getUserInfo()?.userId!!)
            dialog.show(requireActivity().supportFragmentManager)
            mViewModel.requestNowMatch()
            DataPointUtil.reportHomeMatch(SpHelper.getUserInfo()?.userId!!)
        }
        clMatchHide.clickDelay {
            val dialog = MatchingFragmentDialog.newInstance(SpHelper.getUserInfo()?.userId!!)
            dialog.show(requireActivity().supportFragmentManager)
            mViewModel.requestNowMatch()
            DataPointUtil.reportHomeMatch(SpHelper.getUserInfo()?.userId!!)
        }
        clRing.clickDelay {
//            val dialog = MatchFragmentDialog.newInstance(SpHelper.getUserInfo()?.userId!!)
//            dialog.show(requireActivity().supportFragmentManager)
            if (true) {
                showCloseDialog()
            } else {
                toast("Ring已开启，很快就能收到附近的小伙伴发来的信号啦！")
            }
        }
        clRingHide.clickDelay {
//            val dialog = MatchFragmentDialog.newInstance(SpHelper.getUserInfo()?.userId!!)
//            dialog.show(requireActivity().supportFragmentManager)
            if (true) {
                showCloseDialog()
            } else {
                toast("Ring已开启，很快就能收到附近的小伙伴发来的信号啦！")
            }
        }
        tvNow.clickDelay {
            changeTab(0)
            viewpager.setCurrentItem(0, true)
            DataPointUtil.reportHomeNow(SpHelper.getUserInfo()?.userId!!)
        }
        tvNowHide.clickDelay {
            changeTab(0)
            viewpager.setCurrentItem(0, true)
            DataPointUtil.reportHomeNow(SpHelper.getUserInfo()?.userId!!)
        }
        tvThink.clickDelay {
            changeTab(1)
            viewpager.setCurrentItem(1, true)
            DataPointUtil.reportHomeThink(SpHelper.getUserInfo()?.userId!!)
        }
        tvThinkHide.clickDelay {
            changeTab(1)
            viewpager.setCurrentItem(1, true)
            DataPointUtil.reportHomeThink(SpHelper.getUserInfo()?.userId!!)
        }
        ivAdd.clickDelay {
            val dialog = PushFragmentDialog.newInstance()
            dialog.show(requireActivity().supportFragmentManager)
            DataPointUtil.reportPublish(SpHelper.getUserInfo()?.userId!!)
        }


        scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            var scale = scrollY / dp2px(112f).toFloat()
            if (scale >= 1) {
                scale = 1f
            }
            clTopHide.alpha = scale
            clTop.alpha = 1 - scale
            if (scrollY >= dp2px(56f)) {
                clTopHide.visible()
            } else {
                clTopHide.gone()
            }
            val location = IntArray(2)
            mBinding.clTab.getLocationOnScreen(location)
            val locationY = location[1]
            if (locationY >= dp2px(172f)) {
                mBinding.clTabHide.gone()
            } else {
                mBinding.clTabHide.visible()
            }
        }


        val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        initBanner(list)
        initRv()
        initFragment()
    }

    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is UserInfoBean -> {
                    val bundle = Bundle().apply {
                        putString("userId", it.userId)
                    }
                    EMClientHelper.saveUser(
                        UserEntity(
                            it.userId,
                            it.userName,
                            it.avatar
                        )
                    )
                    jumpARoute(RouteUrl.ChatModule.ACTIVITY_CHAT_DETAIL, bundle)
                }
            }
        }
    }

    override fun initRequestData() {

    }

    fun changeTab(position: Int) {
        if (position == 0) {
            mBinding.tvNow.setTextColor(R.color._3A3A3A.getResColor())
            mBinding.tvNow.textSize = 20f
            mBinding.tvThink.setTextColor(R.color._803A3A3A.getResColor())
            mBinding.tvThink.textSize = 14f
            mBinding.tvNowHide.setTextColor(R.color._3A3A3A.getResColor())
            mBinding.tvNowHide.textSize = 20f
            mBinding.tvThinkHide.setTextColor(R.color._803A3A3A.getResColor())
            mBinding.tvThinkHide.textSize = 14f
        } else {
            mBinding.tvNow.setTextColor(R.color._803A3A3A.getResColor())
            mBinding.tvNow.textSize = 14f
            mBinding.tvThink.setTextColor(R.color._3A3A3A.getResColor())
            mBinding.tvThink.textSize = 20f
            mBinding.tvNowHide.setTextColor(R.color._803A3A3A.getResColor())
            mBinding.tvNowHide.textSize = 14f
            mBinding.tvThinkHide.setTextColor(R.color._3A3A3A.getResColor())
            mBinding.tvThinkHide.textSize = 20f
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
        val mData = arrayListOf<TabItemBean>()
        mData.add(TabItemBean("正在", NowFragment::class.java.getName()))
        mData.add(TabItemBean("想法", ThinkFragment::class.java.getName()))
        mBinding.viewpager.adapter =
            TabAdapter(requireActivity(), requireActivity().supportFragmentManager, mData)

        mBinding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                changeTab(position)
                mBinding.viewpager.requestLayout()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    /**
     * 初始化banner
     */
    private fun initBanner(list: ArrayList<String>) {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        mBinding.banner.setPages(
            object : CBViewHolderCreator {
                override fun createHolder(itemView: View): LocalImageHolderView {
                    return LocalImageHolderView(itemView)
                }

                override fun getLayoutId(): Int {
                    return R.layout.item_banner
                }
            }, list
        ) //设置指示器的方向
            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL) //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
            .setPageIndicator(
                intArrayOf(
                    R.drawable.shape_circle_indicator_off,
                    R.drawable.shape_circle_indicator_on
                )
            )
            .setOnItemClickListener(OnItemClickListener { position ->
                val bundle = Bundle().apply {
                    putString("webUrl", HttpBaseUrlConstant.BASE_URL + "page/user-agreement")
                    putString("webTitle", "用户协议")
                }
                jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
            }).startTurning()
    }

    /**
     * banner的Item
     */
    class LocalImageHolderView(itemView: View?) :
        Holder<String>(itemView) {
        private var imageView: ImageView? = null
        override fun initView(itemView: View) {
            imageView = itemView.findViewById(R.id.iv_banner)
        }

        override fun updateUI(data: String) {
        }
    }

    fun initRv() {
        mAdapter = HomeMatchAdapter()
        mBinding.rvMatch.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, dp2px(10f), 0,
                    ContextCompat.getColor(context, R.color.transparent)
                )
            )
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
            adapter = mAdapter
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_label_bg -> {

                }
            }
        }
        mAdapter.addData(NowTagBean("", 1f, "", "", "", "", "", 1f))
        mAdapter.addData(NowTagBean("", 1f, "", "", "", "", "", 1f))
        mAdapter.addData(NowTagBean("", 1f, "", "", "", "", "", 1f))
        mAdapter.addData(NowTagBean("", 1f, "", "", "", "", "", 1f))
    }

    /**
     * 显示关闭弹框
     */
    fun showCloseDialog() {
        DialogHelper.showMultipleDialog(
            requireActivity(),
            "关闭后你将错过附近的小伙伴发来的Ring信号哦",
            "保持开启",
            "确认关闭",
            {

            },
            {

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
        CoilUtils.loadRoundBorder(
            mBinding.ivAvatarHide,
            userInfoBean?.avatar!!,
            17f,
            1f,
            R.color.white.getResColor()
        )
        mBinding.tvNameHide.setText(userInfoBean.userName)
    }

    //切换
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showCall(event: ChangeThinkEvent) {
        mBinding.viewpager.setCurrentItem(event.position)
    }

}
