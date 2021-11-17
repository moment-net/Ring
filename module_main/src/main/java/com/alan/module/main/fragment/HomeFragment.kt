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
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.*
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.db.entity.UserEntity
import com.alan.mvvm.common.dialog.DialogHelper
import com.alan.mvvm.common.event.ChangeThinkEvent
import com.alan.mvvm.common.event.RefreshEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.report.DataPointUtil
import com.alan.mvvm.common.ui.BaseFragment
import com.alan.mvvm.common.views.GuideView
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
    lateinit var matchInfoBean: MatchInfoBean
    var gender: Int = 1
    var isOpenRing: Boolean = false

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
            val dialog = FilterFragmentDialog.newInstance(gender)
            dialog.show(requireActivity().supportFragmentManager)
        }
        ivFilterHide.clickDelay {
            val dialog = FilterFragmentDialog.newInstance(gender)
            dialog.show(requireActivity().supportFragmentManager)
        }
        clMatch.clickDelay {
            val dialog = MatchingFragmentDialog.newInstance("")
            dialog.show(requireActivity().supportFragmentManager)
            DataPointUtil.reportHomeMatch(SpHelper.getUserInfo()?.userId!!)
        }
        clMatchHide.clickDelay {
            val dialog = MatchingFragmentDialog.newInstance("")
            dialog.show(requireActivity().supportFragmentManager)
            DataPointUtil.reportHomeMatch(SpHelper.getUserInfo()?.userId!!)
        }
        clRing.clickDelay {
            if (isOpenRing) {
                showCloseDialog()
            } else {
                mViewModel.requestMatchJoin()
            }
            DataPointUtil.reportClickRing(isOpenRing)
        }
        clRingHide.clickDelay {
            if (isOpenRing) {
                showCloseDialog()
            } else {
                mViewModel.requestMatchJoin()
            }
            DataPointUtil.reportClickRing(isOpenRing)
        }
        ivNow.clickDelay {
            changeTab(0)
            viewpager.setCurrentItem(0, true)
            DataPointUtil.reportHomeNow(SpHelper.getUserInfo()?.userId!!)
        }
        ivNowHide.clickDelay {
            changeTab(0)
            viewpager.setCurrentItem(0, true)
            DataPointUtil.reportHomeNow(SpHelper.getUserInfo()?.userId!!)
        }
        ivThink.clickDelay {
            changeTab(1)
            viewpager.setCurrentItem(1, true)
            DataPointUtil.reportHomeThink(SpHelper.getUserInfo()?.userId!!)
        }
        ivThinkHide.clickDelay {
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

//        val options = HighlightOptions.Builder()
//            .setOnHighlightDrewListener { canvas, rectF ->
//                val paint = Paint()
//                paint.setColor(Color.WHITE)
//                paint.setStyle(Paint.Style.STROKE)
//                paint.setStrokeWidth(3f)
//                paint.setPathEffect(DashPathEffect(floatArrayOf(10f, 10f), 0f))
//                canvas.drawRoundRect(rectF,30f,30f, paint)
//            }
//            .build()
//        NewbieGuide.with(requireActivity())
//            .setLabel("0000")
//            .setShowCounts(1)
//            .alwaysShow(true)
//            .addGuidePage(GuidePage()
//                .addHighLight(mBinding.clMatch,HighLight.Shape.ROUND_RECTANGLE,30,10,
//                    RelativeGuide(R.layout.layout_guide_match,Gravity.BOTTOM)
//                )
//                .addHighLightWithOptions(mBinding.clMatch,options)
//            )
//            .show()


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

                is MatchInfoBean -> {
                    matchInfoBean = it
                    mBinding.tvRingNum.setText("今日剩余${matchInfoBean.times}次")
                    mBinding.tvRingNumHide.setText("今日剩余${matchInfoBean.times}次")
                    isOpenRing = it.status == 1
                    changeStatus(isOpenRing)
                    gender = it.setting.sex
                }

                1 -> {
                    //开始匹配
                    isOpenRing = true
                    changeStatus(isOpenRing)
                    toast("Ring已开启，很快就能收到附近的小伙伴发来的信号啦！")
                }
                2 -> {
                    //停止匹配
                    isOpenRing = false
                    changeStatus(isOpenRing)
                }
            }
        }

        mViewModel.ldCard.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    val list = it.data as ArrayList<CardTagBean>

                    mAdapter.setList(list)
                }

            }
        }

        mViewModel.ldBanner.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    val list = it.data as ArrayList<BannerBean>

                    initBanner(list)
                }

            }
        }
    }

    override fun initRequestData() {
        if (SpHelper.getNewUser()) {
            showMatch()
        }
    }

    fun showMatch() {
        val guideView = GuideView.Builder
            .newInstance(requireActivity())
            .setTargetView(mBinding.clMatch) //设置目标
            .setDirction(GuideView.Direction.BOTTOM)
            .setShape(GuideView.MyShape.BITMAP)
            .setRoundRadius(dp2px(20f))
            .setBgLocation(intArrayOf(dp2px(106f), dp2px(62f)))
            .setCenterPadding(intArrayOf(dp2px(78f), dp2px(38f), dp2px(78f), dp2px(32f)))
            .setBgImage(R.drawable.icon_home_guide_one)
            .setLayoutResId(R.layout.layout_guide_match)
            .setOnclickExit(true)
            .setOnclickListener {
                showThink()
            }
            .build()
        guideView.show()
    }

    fun showThink() {
        val guideView = GuideView.Builder
            .newInstance(requireActivity())
            .setTargetView(mBinding.ivThink) //设置目标
            .setDirction(GuideView.Direction.BOTTOM)
            .setShape(GuideView.MyShape.RECTANGULAR)
            .setRoundRadius(dp2px(6f))
            .setBgLocation(intArrayOf(dp2px(54f), dp2px(44f)))
            .setCenterPadding(intArrayOf(dp2px(33f), dp2px(21f), dp2px(33f), dp2px(21f)))
            .setBgImage(R.drawable.icon_home_guide_two)
            .setLayoutResId(R.layout.layout_guide_think)
            .setOnclickExit(true)
            .setOnclickListener {
                showPush()
            }
            .build()
        guideView.show()
    }

    fun showPush() {
        val guideView = GuideView.Builder
            .newInstance(requireActivity())
            .setTargetView(mBinding.ivAdd) //设置目标
            .setDirction(GuideView.Direction.BOTTOM)
            .setShape(GuideView.MyShape.RECTANGULAR)
            .setRoundRadius(dp2px(10f))
            .setBgLocation(intArrayOf(dp2px(59f), dp2px(72f)))
            .setCenterPadding(intArrayOf(dp2px(37f), dp2px(48f), dp2px(37f), dp2px(47f)))
            .setBgImage(R.drawable.icon_home_guide_three)
            .setLayoutResId(R.layout.layout_guide_push)
            .setOnclickExit(true)
            .setOnclickListener {

            }
            .build()
        guideView.show()
    }

    fun changeTab(position: Int) {
        if (position == 0) {
            mBinding.ivNow.setImageResource(R.drawable.icon_home_title_now_big)
            mBinding.ivNowHide.setImageResource(R.drawable.icon_home_title_now_big)
            mBinding.ivThink.setImageResource(R.drawable.icon_home_title_think)
            mBinding.ivThinkHide.setImageResource(R.drawable.icon_home_title_think)
        } else {
            mBinding.ivNow.setImageResource(R.drawable.icon_home_title_now)
            mBinding.ivNowHide.setImageResource(R.drawable.icon_home_title_now)
            mBinding.ivThink.setImageResource(R.drawable.icon_home_title_think_big)
            mBinding.ivThinkHide.setImageResource(R.drawable.icon_home_title_think_big)
        }
    }

    fun changeStatus(isOpen: Boolean) {
        if (isOpen) {
            mBinding.tvStatus.setText("ON")
            mBinding.tvStatusHide.setText("ON")
        } else {
            mBinding.tvStatus.setText("OFF")
            mBinding.tvStatusHide.setText("OFF")
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
    private fun initBanner(list: ArrayList<BannerBean>) {
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
                    putString("webUrl", list.get(position).url)
                    putString("webTitle", list.get(position).title)
                }
                jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
                DataPointUtil.reportCLickBanner(list.get(position).title)
            })
        if (list.size > 1) {
            mBinding.banner.setPointViewVisible(true)
            mBinding.banner.startTurning()
        } else {
            mBinding.banner.setPointViewVisible(false)
        }
    }

    /**
     * banner的Item
     */
    class LocalImageHolderView(itemView: View?) :
        Holder<BannerBean>(itemView) {
        private var imageView: ImageView? = null
        override fun initView(itemView: View) {
            imageView = itemView.findViewById(R.id.iv_banner)
        }

        override fun updateUI(data: BannerBean) {
            CoilUtils.load(imageView!!, data.bgUrl)
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
            val bean = mAdapter.data.get(position)
            when (view.id) {
                R.id.tv_label_bg -> {
                    val dialog = MatchingFragmentDialog.newInstance(bean.tag)
                    dialog.show(requireActivity().supportFragmentManager)
                    DataPointUtil.reportClickFastMatch(bean.tag)
                }
            }
        }
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
                DataPointUtil.reportClickStartMatch()
            },
            {
                DataPointUtil.reportClickStopMatch()
                mViewModel.requestMatchStop()
            })
    }

    //切换
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showCall(event: ChangeThinkEvent) {
        mBinding.viewpager.setCurrentItem(event.position)
    }

    //切换
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refresh(event: RefreshEvent) {
        mViewModel.requestMatchInfo()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mViewModel.requestBanner()
            mViewModel.requestCardAllList()
            mViewModel.requestMatchInfo()
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.requestBanner()
        mViewModel.requestCardAllList()
        mViewModel.requestMatchInfo()
        setUserInfo()
    }


}
