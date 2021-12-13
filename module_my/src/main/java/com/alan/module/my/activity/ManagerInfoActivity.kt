package com.alan.module.home.activity

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.my.R
import com.alan.module.my.adapter.CardAdapter
import com.alan.module.my.adapter.TaThinkAdapter
import com.alan.module.my.databinding.ActivityManagerInfoBinding
import com.alan.module.my.dialog.CardInfoFragmentDialog
import com.alan.module.my.dialog.CompleteFragmentDialog
import com.alan.module.my.viewmodol.ManagerInfoViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.CardInfoBean
import com.alan.mvvm.base.http.responsebean.ThinkBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.IMConstant
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.db.entity.UserEntity
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.report.DataPointUtil
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.hyphenate.chat.EMMessage
import com.socks.library.KLog
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：饭友详情
 */
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_MANAGER)
@AndroidEntryPoint
class ManagerInfoActivity : BaseActivity<ActivityManagerInfoBinding, ManagerInfoViewModel>() {

    @JvmField
    @Autowired
    var userId: String = ""
    lateinit var userInfoBean: UserInfoBean
    lateinit var cardInfoBean: CardInfoBean
    lateinit var mAdapter: TaThinkAdapter
    lateinit var mCardAdapter: CardAdapter
    var isLoad = false
    var mCursor: Int = 0

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<ManagerInfoViewModel>()

    /**
     * 初始化View
     */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun ActivityManagerInfoBinding.initView() {
        ivBack.clickDelay { finish() }

        tvChat.clickDelay {
            val bundle = Bundle().apply {
                putString("userId", userInfoBean.userId)
            }
            EMClientHelper.saveUser(
                UserEntity(
                    userInfoBean.userId,
                    userInfoBean.userName,
                    userInfoBean.avatar
                )
            )
            jumpARoute(RouteUrl.ChatModule.ACTIVITY_CHAT_DETAIL, bundle)
            DataPointUtil.reportChat(
                SpHelper.getUserInfo()?.userId!!,
                userInfoBean.userId,
                userInfoBean.onlineStatus!!
            )
        }

        mBinding.llTitle.setBackgroundColor(Color.argb(0, 255, 255, 255))
        initScrollView()

        initRvCard()
        initRv()

        startAnimator()
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is UserInfoBean -> {
                    userInfoBean = it
                    setUserInfo()
                }

                is Int -> {
                    userInfoBean?.followStatus = it
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

        mViewModel.ldCard.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    mCursor = it.cursor
                    val list: ArrayList<CardInfoBean> = it.data as ArrayList<CardInfoBean>
                    mCardAdapter.setList(list)
                    if (list.isEmpty()) {
                        mBinding.rvCard.gone()
                    } else {
                        mBinding.rvCard.visible()
                    }


                }
            }
        }

        mViewModel.ldSelf.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    val list: ArrayList<CardInfoBean> = it.data as ArrayList<CardInfoBean>
                    if (list.isEmpty()) {
                        val dialog = CompleteFragmentDialog.newInstance()
                        dialog.show(supportFragmentManager)
                    } else {
                        val dialog =
                            CardInfoFragmentDialog.newInstance(userId, cardInfoBean.cardName!!)
                        dialog.show(supportFragmentManager)
                    }
                }
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

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
            } else {
                mBinding.ivBack.setImageResource(R.drawable.icon_back_white)
            }
            KLog.d("xujm", "locationY:$scrollY===scale:$scale")
            val alpha = (255 * scale).toInt()
            mBinding.llTitle.setBackgroundColor(Color.argb(alpha, 255, 255, 255))
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
                FlexboxLayoutManager(this@ManagerInfoActivity, FlexDirection.ROW, FlexWrap.WRAP)
            adapter = mCardAdapter
        }

        mCardAdapter.setOnItemChildClickListener { adapter, view, position ->
            cardInfoBean = mCardAdapter.data.get(position)
            when (view.id) {
                R.id.tv_label_bg -> {
                    DataPointUtil.reportClickOtherCard(cardInfoBean.id!!)
                    mViewModel.requestSelfCardList(SpHelper.getUserInfo()?.userId!!)
                }
            }
        }
    }

    fun initRv() {
        mAdapter = TaThinkAdapter(this@ManagerInfoActivity)
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
        mAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            mBinding.rvList.postDelayed(Runnable {
                isLoad = true
                requestList()
            }, 1000)
        }
    }

    fun startAnimator() {
        val objectAnimator =
            ObjectAnimator.ofFloat(mBinding.tvDoing, "translationY", 0f, dp2px(10f).toFloat(), 0f)
        objectAnimator.repeatCount = ObjectAnimator.INFINITE
        objectAnimator.duration = 2000
        objectAnimator.start()
    }

    fun setUserInfo() {
        if (userInfoBean == null) {
            return
        }
        val userInfoBean = userInfoBean;

        if (!TextUtils.equals(userInfoBean.userId, SpHelper.getUserInfo()?.userId)) {
            mBinding.tvChat.visible()
        } else {
            mBinding.tvChat.gone()
        }

        val url = if (userInfoBean?.avatar!!.contains("?")) {
            userInfoBean?.avatar!!.split("?")[0]
        } else {
            userInfoBean?.avatar!!
        }
        CoilUtils.load(mBinding.ivAvatarBg, url)
        CoilUtils.loadCircle(mBinding.ivAvatar, userInfoBean?.avatar!!)
        mBinding.tvTitle.setText(userInfoBean?.userName)
        mBinding.tvName.setText(userInfoBean?.userName)
        if (TextUtils.isEmpty(userInfoBean?.recentDoing)) {
            mBinding.tvDoing.gone()
        } else {
            mBinding.tvDoing.visible()
            mBinding.tvDoing.setText(userInfoBean?.recentDoing)
        }
        var address = ""
        if (!TextUtils.isEmpty(userInfoBean?.address) && userInfoBean?.address!!.split("-").size == 3) {
            address = userInfoBean?.address!!.split("-")[1]
        }
        val age = if (userInfoBean.age > 0) {
            "${userInfoBean.age}岁"
        } else {
            ""
        }
        if (TextUtils.isEmpty(age) && TextUtils.isEmpty(address)) {
            mBinding.tvAge.setText("")
            mBinding.tvAge.compoundDrawablePadding = 0
        } else {
            mBinding.tvAge.setText("$age $address")
            mBinding.tvAge.compoundDrawablePadding = dp2px(2f)
        }
        if (userInfoBean.gender == 1) {
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


        mBinding.tvDesc.setText(userInfoBean.desc)
    }


    override fun onResume() {
        super.onResume()
        mViewModel.requestUserInfo(userId!!)
        mViewModel.requestCardList(userId!!)
        requestRefresh()
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
        mViewModel.requestList(mCursor, userId!!)
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