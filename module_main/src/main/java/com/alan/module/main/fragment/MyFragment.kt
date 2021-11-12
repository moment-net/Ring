package com.alan.module.main.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.main.R
import com.alan.module.main.adapter.CardAdapter
import com.alan.module.main.adapter.MyThinkAdapter
import com.alan.module.main.databinding.FragmentMyBinding
import com.alan.module.main.viewmodel.MyViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.CardInfoBean
import com.alan.mvvm.base.http.responsebean.DiamondBean
import com.alan.mvvm.base.http.responsebean.ThinkBean
import com.alan.mvvm.base.http.responsebean.UnreadBean
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.report.DataPointUtil
import com.alan.mvvm.common.ui.BaseFragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.socks.library.KLog
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：
 */
@AndroidEntryPoint
class MyFragment : BaseFragment<FragmentMyBinding, MyViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance() =
            MyFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override val mViewModel by viewModels<MyViewModel>()
    lateinit var mAdapter: MyThinkAdapter
    lateinit var mCardAdapter: CardAdapter
    var listener: OnClickFinishListener? = null
    var isShow: Boolean = false
    var isLoad = false
    var mCursor: Int = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun FragmentMyBinding.initView() {
        ivBack.clickDelay {
            if (listener != null) {
                listener!!.onClick()
            }
        }
        ivSet.clickDelay {
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_SET)
        }
        ivMsg.clickDelay {
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_MSG)
        }
        ivEdit.clickDelay {
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_PERSONINFO)
        }

        clDiamond.clickDelay {
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_DIAMOND)
            DataPointUtil.reportMyDiamond(SpHelper.getUserInfo()?.userId!!)
        }
        clWallet.clickDelay { jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_WALLET) }

        mBinding.llTitle.setBackgroundColor(Color.argb(0, 255, 255, 255))
        if (isShow) {
            mBinding.ivBack.visible()
        } else {
            mBinding.ivBack.visibility = View.INVISIBLE
        }

        initScrollView()


        initRv()

        initRvCard()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initScrollView() {
        mBinding.scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            var scale: Float = (scrollY / dp2px(320f).toFloat())
            if (scale > 1) {
                scale = 1f
            }
            if (scale == 1f) {
                mBinding.ivBack.setImageResource(R.drawable.icon_back)
                mBinding.ivMsg.setImageResource(R.drawable.icon_my_msg_black)
                mBinding.ivSet.setImageResource(R.drawable.icon_my_set_black)
            } else {
                mBinding.ivBack.setImageResource(R.drawable.icon_back_white)
                mBinding.ivMsg.setImageResource(R.drawable.icon_my_msg)
                mBinding.ivSet.setImageResource(R.drawable.icon_my_set)
            }
            KLog.d("xujm", "locationY:$scrollY===scale:$scale")
            val alpha = (255 * scale).toInt()
            mBinding.llTitle.setBackgroundColor(Color.argb(alpha, 255, 255, 255))
        }
    }

    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is DiamondBean -> {
                    if (it.recharge) {
                        mBinding.ivFirst.gone()
                        mBinding.tvDiamondNum.visible()
                        mBinding.tvDiamondNum.setText("${it.points}个钻石")
                    } else {
                        mBinding.ivFirst.visible()
                        mBinding.tvDiamondNum.gone()
                    }
                }

                is UnreadBean -> {
                    if (it.newNoticeTotal > 0) {
                        mBinding.ivRed.visible()
                    } else {
                        mBinding.ivRed.gone()
                    }
                }

                is BaseResponse<*> -> {
                    mCursor = it.cursor
                    val list: ArrayList<ThinkBean> = it.data as ArrayList<ThinkBean>
                    if (isLoad) {
                        mAdapter.addData(list)
                    } else {
                        mAdapter.setList(list)
                    }
                    if (it.hasMore) {
                        mAdapter.loadMoreModule.loadMoreComplete()
                    } else {
                        mAdapter.loadMoreModule.loadMoreEnd()
                    }

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
                        true
                    }
                    mAdapter.notifyItemChanged(position)
                }
            }
        }
        mViewModel.ldCard.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    mCursor = it.cursor
                    val list: ArrayList<CardInfoBean> = it.data as ArrayList<CardInfoBean>
                    list.add(CardInfoBean())
                    mCardAdapter.setList(list)

                }
            }
        }
    }

    override fun initRequestData() {

    }

    override fun onResume() {
        super.onResume()
        mViewModel.requestDiamond()
        mViewModel.requestUnRead()
        mViewModel.requestCardList(SpHelper.getUserInfo()?.userId!!)
        requestRefresh()
        setUserInfo()
    }


    fun setUserInfo() {
        val userInfo = SpHelper.getUserInfo()
        val url = if (SpHelper.getUserInfo()?.avatar!!.contains("?")) {
            SpHelper.getUserInfo()?.avatar!!.split("?")[0]
        } else {
            SpHelper.getUserInfo()?.avatar!!
        }
        CoilUtils.load(mBinding.ivAvatarBg, url)
        CoilUtils.loadCircle(
            mBinding.ivAvatar,
            SpHelper.getUserInfo()?.avatar!!
        )
        mBinding.tvName.setText(userInfo?.userName)

        var address = ""
        if (!TextUtils.isEmpty(userInfo?.address) && userInfo?.address!!.split("-").size == 3) {
            address = userInfo?.address!!.split("-")[1]
        }
        val age = if (userInfo?.age!! > 0) {
            "${userInfo.age}岁"
        } else {
            ""
        }
        if (TextUtils.isEmpty(address) && TextUtils.isEmpty(age)) {
            mBinding.tvAge.setText("")
            mBinding.tvAge.compoundDrawablePadding = 0
        } else {
            mBinding.tvAge.setText("$age $address")
            mBinding.tvAge.compoundDrawablePadding = dp2px(2f)
        }
        if (userInfo.gender == 1) {
            mBinding.tvAge.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.icon_home_boy_blue,
                0,
                0,
                0
            )
            mBinding.tvAge.setTextColor(R.color._7F89FF.getResColor())
        } else {
            mBinding.tvAge.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.icon_home_girl_blue,
                0,
                0,
                0
            )
            mBinding.tvAge.setTextColor(R.color._F27E9A.getResColor())
        }

    }

    fun initRvCard() {
        mCardAdapter = CardAdapter()
        mBinding.rvCard.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, dp2px(10f), dp2px(10f),
                    ContextCompat.getColor(context, R.color.white)
                )
            )
            layoutManager =
                FlexboxLayoutManager(requireActivity(), FlexDirection.ROW, FlexWrap.WRAP)
            adapter = mCardAdapter
        }

        mCardAdapter.setOnItemChildClickListener { adapter, view, position ->
            val cardInfoBean = mCardAdapter.data.get(position)
            when (view.id) {
                R.id.tv_label_bg -> {
                    val bundle = Bundle().apply {
                        putString("userId", SpHelper.getUserInfo()?.userId!!)
                        putString("name", cardInfoBean.cardName)
                    }
                    jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_CARDSET, bundle)
                }

                R.id.tv_add -> {
                    DataPointUtil.reportClickAddCard()
                    jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_CARD)
                }
            }
        }
    }

    fun initRv() {
        mAdapter = MyThinkAdapter()
        //防止点击闪烁
        mAdapter.setHasStableIds(true)
        mBinding.rvList.itemAnimator = null
        mBinding.rvList.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0, dp2px(30f),
                    ContextCompat.getColor(context, R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_zan -> {
                    val item = mAdapter.data.get(position)
                    val favorite = item.isFavorite
                    if (favorite) {
                        mViewModel.requestZan(item.id, 0, position)
                    } else {
                        mViewModel.requestZan(item.id, 1, position)
                    }
                }
            }
        }

        mAdapter.setEmptyView(TextView(activity).apply {
            setText("当前还没有想法，快去发布想法吧！")
            setTextSize(14f)
            setTextColor(R.color._263A3A3A.getResColor())
            gravity = Gravity.CENTER
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(260f)
            )
        })
        mAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            mBinding.rvList.postDelayed(Runnable {
                isLoad = true
                requestList()
            }, 1000)
        }
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
        mViewModel.requestList(mCursor, SpHelper.getUserInfo()?.userId!!)
    }

    interface OnClickFinishListener {
        fun onClick()
    }
}