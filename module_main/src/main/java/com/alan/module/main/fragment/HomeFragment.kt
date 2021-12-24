package com.alan.module.main.fragment

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.*
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.home.dialog.MatchingFragmentDialog
import com.alan.module.main.R
import com.alan.module.main.adapter.ThinkAdapter
import com.alan.module.main.databinding.FragmentHomeBinding
import com.alan.module.main.dialog.FilterFragmentDialog
import com.alan.module.main.dialog.PushFragmentDialog
import com.alan.module.main.viewmodel.HomeViewModel
import com.alan.mvvm.base.BaseApplication
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.BannerBean
import com.alan.mvvm.base.http.responsebean.MatchInfoBean
import com.alan.mvvm.base.http.responsebean.ThinkBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.IMConstant
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.db.entity.UserEntity
import com.alan.mvvm.common.dialog.DialogHelper
import com.alan.mvvm.common.event.RefreshEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.http.exception.BaseHttpException
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.im.utils.VoicePlayerUtil
import com.alan.mvvm.common.report.DataPointUtil
import com.alan.mvvm.common.ui.BaseFragment
import com.alan.mvvm.common.views.GuideView
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.bigkoo.convenientbanner.listener.OnItemClickListener
import com.hyphenate.chat.EMMessage
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.socks.library.KLog
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

    override val mViewModel by viewModels<HomeViewModel>()
    lateinit var matchInfoBean: MatchInfoBean
    var gender: Int = 1
    var isOpenRing: Boolean = false

    lateinit var mAdapter: ThinkAdapter
    var isLoad = false
    var mCursor: Int = 0
    lateinit var popupWindow: PopupWindow
    lateinit var voicePlayerUtil: VoicePlayerUtil
    private lateinit var ivAvatar: ImageView
    private lateinit var ivFilter: ImageView
    private lateinit var clTop: ConstraintLayout
    private lateinit var clMatch: ConstraintLayout
    private lateinit var clRing: ConstraintLayout
    private lateinit var ivTest: ImageView
    private lateinit var ivChange: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvRingNum: TextView
    private lateinit var tvStatus: TextView
    private lateinit var banner: ConvenientBanner<BannerBean>


    @RequiresApi(Build.VERSION_CODES.M)
    override fun FragmentHomeBinding.initView() {
        ivAvatarHide.clickDelay {
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MY)
            DataPointUtil.reportHomeMy(SpHelper.getUserInfo()?.userId!!)
        }
        ivFilterHide.clickDelay {
            val dialog = FilterFragmentDialog.newInstance(gender)
            dialog.show(requireActivity().supportFragmentManager)
        }
        clMatchHide.clickDelay {
            val dialog = MatchingFragmentDialog.newInstance("")
            dialog.show(requireActivity().supportFragmentManager)
            DataPointUtil.reportHomeMatch(SpHelper.getUserInfo()?.userId!!)
        }
        clRingHide.clickDelay {
            if (isOpenRing) {
                showCloseDialog()
            } else {
                mViewModel.requestMatchJoin()
            }
            DataPointUtil.reportClickRing(isOpenRing)
        }
        ivAdd.clickDelay {
            val dialog = PushFragmentDialog.newInstance()
            dialog.show(requireActivity().supportFragmentManager)
            DataPointUtil.reportPublish(SpHelper.getUserInfo()?.userId!!)
        }

        initRV()

        voicePlayerUtil = VoicePlayerUtil.getInstance(BaseApplication.mContext)
    }

    @SuppressLint("NewApi")
    fun initRV() {
        mAdapter = ThinkAdapter(requireActivity())
        //防止点击闪烁
        mAdapter.setHasStableIds(true)
        mBinding.rvList.itemAnimator = null
        mBinding.rvList.apply {
            addItemDecoration(
                MyColorDecoration(
                    0,
                    0,
                    0,
                    dp2px(15f),
                    R.color.transparent.getResColor()
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val item = mAdapter.data.get(position)
            val userId = item.user.userId
            when (view.id) {
                R.id.iv_avatar -> {
                    val bundle = Bundle().apply {
                        putString("userId", userId)
                    }
                    jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_MANAGER, bundle)
                }
                R.id.iv_more -> {
                    showPopupWindow(view, item.id, userId, position)
                    DataPointUtil.reportHomeMenu(SpHelper.getUserInfo()?.userId!!)
                }
                R.id.iv_zan -> {
                    val favorite = item.isFavorite
                    if (favorite) {
                        mViewModel.requestZan(item.id, 0, position)
                    } else {
                        mViewModel.requestZan(item.id, 1, position)
                    }
                    DataPointUtil.reportLike(SpHelper.getUserInfo()?.userId!!, userId)
                    EMClientHelper.saveUser(
                        UserEntity(
                            userId,
                            item.user.userName,
                            item.user.avatar
                        )
                    )
                }
            }
        }

//        mAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
//        mAdapter.loadMoreModule.setOnLoadMoreListener {
//            mBinding.rvList.postDelayed(Runnable {
//                isLoad = true
//                requestList()
//            }, 1000)
//        }

        mBinding.srlList.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                isLoad = true
                requestList()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                requestRefresh()
            }
        })


        //添加头布局
        val ll_top: View = View.inflate(requireActivity(), R.layout.layout_home_top, null)
        ivAvatar = ll_top.findViewById<ImageView>(R.id.iv_avatar)
        tvName = ll_top.findViewById<TextView>(R.id.tv_name)
        tvRingNum = ll_top.findViewById<TextView>(R.id.tv_ring_num)
        tvStatus = ll_top.findViewById<TextView>(R.id.tv_status)
        banner = ll_top.findViewById<ConvenientBanner<BannerBean>>(R.id.banner)
        ivFilter = ll_top.findViewById<ImageView>(R.id.iv_filter)
        clTop = ll_top.findViewById<ConstraintLayout>(R.id.cl_top)
        clMatch = ll_top.findViewById<ConstraintLayout>(R.id.cl_match)
        clRing = ll_top.findViewById<ConstraintLayout>(R.id.cl_ring)
        ivTest = ll_top.findViewById<ImageView>(R.id.iv_test)
        ivChange = ll_top.findViewById<ImageView>(R.id.iv_change)

        ivAvatar.clickDelay {
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MY)
            DataPointUtil.reportHomeMy(SpHelper.getUserInfo()?.userId!!)
        }

        ivFilter.clickDelay {
//            val dialog = StateFragmentDialog.newInstance()
//            dialog.show(requireActivity().supportFragmentManager)
            val dialog = FilterFragmentDialog.newInstance(gender)
            dialog.show(requireActivity().supportFragmentManager)
        }

        clMatch.clickDelay {
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


        ivTest.clickDelay {
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_SOUND)
        }

        ivChange.clickDelay {
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_SOUNDCHANGE)
        }


//        mBinding.rvList.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            var scale = scrollY / dp2px(116f).toFloat()
//            if (scale >= 1) {
//                scale = 1f
//            }
//            mBinding.clTopHide.alpha = scale
//            clTop.alpha = 1 - scale
//            if (scrollY >= dp2px(58f)) {
//                mBinding.clTopHide.visible()
//            } else {
//                mBinding.clTopHide.gone()
//            }
////            if (scrollY >= dp2px(116f)) {
////                clTopHide.visible()
////            } else {
////                clTopHide.gone()
////            }
//
//            mHandler.removeCallbacksAndMessages(null);
//            mHandler.sendEmptyMessageDelayed(0x01, 50);
//        }

        mBinding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    val scrollY = getScollYDistance()
                    KLog.e("xujm", "当前滑动距离为：$scrollY")
                    if (scrollY < dp2px(58f)) {
                        mBinding.rvList.post(Runnable {
                            (mBinding.rvList.layoutManager as LinearLayoutManager?)!!.scrollToPositionWithOffset(
                                0,
                                0
                            )
                            clTop.alpha = 1f
                        })
                    } else if (scrollY < dp2px(116f)) {
                        val dis = dp2px(116f) - scrollY
                        mBinding.rvList.post(Runnable { mBinding.rvList.smoothScrollBy(0, dis); })
                    }

                    startPlay()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                KLog.e("xujm", "当前滑动距离为：$dy")
                var scrollY = getScollYDistance()
                var scale = scrollY / dp2px(116f).toFloat()
                if (scale >= 1) {
                    scale = 1f
                }
                mBinding.clTopHide.alpha = scale
                clTop.alpha = 1 - scale
                if (scrollY >= dp2px(58f)) {
                    mBinding.clTopHide.visible()
                } else {
                    mBinding.clTopHide.gone()
                }

//                mHandler.removeCallbacksAndMessages(null);
//                mHandler.sendEmptyMessageDelayed(0x01, 50);
            }

        })


        mAdapter.addHeaderView(ll_top)

    }

    fun getScollYDistance(): Int {
        val layoutManager = mBinding.rvList.getLayoutManager() as LinearLayoutManager
        val position = layoutManager.findFirstVisibleItemPosition()
        val firstVisiableChildView = layoutManager.findViewByPosition(position)
        val itemHeight = firstVisiableChildView!!.height
        return position * itemHeight - firstVisiableChildView.top
    }

    private val mHandler: Handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0x01 -> {
                    val scrollY = mBinding.rvList.scrollY
                    KLog.e("xujm", "当前滑动距离为：$scrollY")
                    if (scrollY < dp2px(58f)) {
                        mBinding.rvList.smoothScrollBy(0, 0);
                    } else if (scrollY < dp2px(116f)) {
                        mBinding.rvList.smoothScrollBy(0, dp2px(116f));
                    }

                    startPlay()
                }
            }
        }
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
                    tvRingNum.setText("今日剩余${matchInfoBean.times}次")
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

                is BaseResponse<*> -> {
                    mCursor = it.cursor
                    val list: ArrayList<ThinkBean> = it.data as ArrayList<ThinkBean>
                    if (isLoad) {
                        mAdapter.addData(list)
                        mBinding.srlList.finishLoadMore()
                    } else {
                        mAdapter.setList(list)
                        mBinding.srlList.finishRefresh()
                    }


//                    if (it.hasMore) {
//                        mAdapter.loadMoreModule.loadMoreComplete()
//                    } else {
//                        mAdapter.loadMoreModule.loadMoreEnd()
//                    }
                }

                is BaseHttpException -> {
                    toast(it.errorMessage)
                }


                is Int -> {
                    mAdapter.removeAt(it)
                    mAdapter.notifyItemRemoved(it)
                }

                is Pair<*, *> -> {
                    val action = it.first
                    val position = it.second as Int
                    val favoriteCount = mAdapter.data.get(position).favoriteCount
                    mAdapter.data.get(position).isFavorite = if (action == 0) {
                        mAdapter.data.get(position).favoriteCount = favoriteCount - 1
                        false
                    } else {
                        mAdapter.data.get(position).favoriteCount = favoriteCount + 1
                        val user = mAdapter.data.get(position).user
                        sendTextMessage(
                            "我认同了你的想法",
                            user.userId,
                            mAdapter.data.get(position).content
                        )
                        true
                    }
                    mAdapter.notifyItemChanged(position)
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
            .setTargetView(clMatch) //设置目标
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
            .setTargetView(ivTest) //设置目标
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


    fun changeStatus(isOpen: Boolean) {
        if (isOpen) {
            tvStatus.setText("ON")
            mBinding.tvStatusHide.setText("ON")
        } else {
            tvStatus.setText("OFF")
            mBinding.tvStatusHide.setText("OFF")
        }
    }



    /**
     * 初始化banner
     */
    private fun initBanner(list: ArrayList<BannerBean>) {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        banner.setPages(
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
            banner.setPointViewVisible(true)
            banner.startTurning()
        } else {
            banner.setPointViewVisible(false)
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




    fun setUserInfo() {
        val userInfoBean = SpHelper.getUserInfo()
        CoilUtils.loadRoundBorder(
            ivAvatar,
            userInfoBean?.avatar!!,
            17f,
            1f,
            R.color.white.getResColor()
        )
        tvName.setText(userInfoBean.userName)
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


    fun startPlay() {
        if (mAdapter.data.isEmpty()) {
            return
        }
        val layoutManager = mBinding.rvList.layoutManager as LinearLayoutManager
        val findFirstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val findLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        val position = (findFirstVisibleItemPosition + findLastVisibleItemPosition) / 2
        KLog.e("xujm", "当前位置：$position")
        val bean = mAdapter.data.get(position)
        if (voicePlayerUtil.isPlaying) {
            //无论播放的语音项是这个还是其他，都先停止语音播放
            voicePlayerUtil.stop()
            // 停止语音播放动画。
//            stopVoicePlayAnimation()

            // 如果正在播放的语音项是此项，则只需停止播放即可。
            val playingUrl: String = voicePlayerUtil.url
            if (TextUtils.equals(bean.audio, playingUrl)) {
                return
            }
        }
        if (!TextUtils.isEmpty(bean.audio)) {
//            val viewHolderView = mBinding.rvList.getChildAt(position)
//            val tv_content = viewHolderView.findViewById<TextView>(R.id.tv_content)

//            ivVoice = iv_voice
            voicePlayerUtil.play(bean.audio, MediaPlayer.OnCompletionListener {
                KLog.e("xujm", "开始播放声音")
//                stopVoicePlayAnimation()
            })
            // 启动语音播放动画
//            startVoicePlayAnimation()
        }
    }


    /**
     * 显示菜单项
     */
    fun showPopupWindow(view: View, id: String, userId: String, position: Int) {
        val contentview: View =
            LayoutInflater.from(requireActivity()).inflate(R.layout.layout_more_menu, null)
        contentview.isFocusable = true
        contentview.isFocusableInTouchMode = true
        val tvReport = contentview.findViewById<TextView>(R.id.tv_report)
        val tvShield = contentview.findViewById<TextView>(R.id.tv_shield)


        tvReport.clickDelay {
            popupWindow.dismiss()
            val bundle = Bundle().apply {
                putString(
                    "webUrl",
                    HttpBaseUrlConstant.BASE_H5URL + "#/freedomspeak-report?reportFromUserid=${SpHelper.getUserInfo()!!.userId}&reportToUserid=${userId}"
                )
                putString("webTitle", "举报")
            }
            jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
            DataPointUtil.reportReport(SpHelper.getUserInfo()?.userId!!)
        }
        tvShield.clickDelay {
            popupWindow.dismiss()
            mViewModel.requestBanThink(id, position)
            DataPointUtil.reportBlock(SpHelper.getUserInfo()?.userId!!)
        }

        popupWindow = PopupWindow(
            contentview,
            dp2px(88f),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setFocusable(true)
        popupWindow.setOutsideTouchable(false)
        popupWindow.showAsDropDown(view, -dp2px(50f), 0)
    }


    /**
     * 刷新列表
     */
    fun requestRefresh() {
        isLoad = false
        mCursor = 0
        requestList()
    }


    fun requestList() {
        mViewModel.requestList(mCursor)
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
            requestRefresh()
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.requestBanner()
        mViewModel.requestCardAllList()
        mViewModel.requestMatchInfo()
        requestRefresh()
        setUserInfo()
    }

    /**
     * 发送点赞IM消息
     */
    fun sendTextMessage(content: String, userId: String, zan: String) {
        val message = EMMessage.createTxtSendMessage(content, userId)
        // 增加自己特定的属性
        message.setAttribute(IMConstant.MESSAGE_ATTR_AVATAR, SpHelper.getUserInfo()?.avatar)
        message.setAttribute(IMConstant.MESSAGE_ATTR_USERNAME, SpHelper.getUserInfo()?.userName)
        message.setAttribute(IMConstant.MESSAGE_ATTR_ZANCONTENT, zan)

        // 设置自定义扩展字段-强制推送
//        message.setAttribute(IMConstant.MESSAGE_ATTR_FORCEPUSH, true);
        // 设置自定义扩展字段-发送静默消息（不推送）
//        message.setAttribute(IMConstant.MESSAGE_ATTR_IGNOREPUSH, true);

        EMClientHelper.chatManager.sendMessage(message)
    }
}
