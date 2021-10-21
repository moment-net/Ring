package com.alan.module.home.activity

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.home.R
import com.alan.module.home.adapter.TaThinkAdapter
import com.alan.module.home.databinding.ActivityManagerInfoBinding
import com.alan.module.home.viewmodol.ManagerInfoViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.ThinkBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.db.entity.UserEntity
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.im.utils.VoicePlayerUtil
import com.alan.mvvm.common.report.DataPointUtil
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.socks.library.KLog
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：饭友详情
 */
@Route(path = RouteUrl.HomeModule.ACTIVITY_HOME_MANAGER)
@AndroidEntryPoint
class ManagerInfoActivity : BaseActivity<ActivityManagerInfoBinding, ManagerInfoViewModel>() {

    @JvmField
    @Autowired(name = "userId")
    var userId: String? = null
    lateinit var userInfoBean: UserInfoBean
    lateinit var mAdapter: TaThinkAdapter
    var isLoad = false
    var mCursor: Int = 0

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<ManagerInfoViewModel>()
    var countDownTimer: CountDownTimer? = null

    /**
     * 初始化View
     */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun ActivityManagerInfoBinding.initView() {
        ivBack.clickDelay { finish() }
        tvVoice.clickDelay {
            val url = userInfoBean?.greeting?.audioPath
            val duration = userInfoBean?.greeting?.duration ?: 0
            if (VoicePlayerUtil.getInstance(this@ManagerInfoActivity).isPlaying) {
                //无论播放的语音项是这个还是其他，都先停止语音播放
                VoicePlayerUtil.getInstance(this@ManagerInfoActivity).stop()
                // 停止语音播放动画。
                stopVoicePlayAnimation()

                // 如果正在播放的语音项是此项，则只需停止播放即可。
                val playingUrl: String = VoicePlayerUtil.getInstance(this@ManagerInfoActivity).url
                if (TextUtils.equals(url, playingUrl)) {
                    return@clickDelay
                }
            }
            if (!TextUtils.isEmpty(url) && duration != 0) {
                VoicePlayerUtil.getInstance(this@ManagerInfoActivity)
                    .play(url, MediaPlayer.OnCompletionListener {
                        stopVoicePlayAnimation()
                    })
                // 启动语音播放动画
                startVoicePlayAnimation(duration)
            }
        }
//        tvFocus.clickDelay {
//            if (userInfoBean?.followStatus == 0) {
//                mViewModel.requestChangeFollow(userInfoBean?.userId!!, 1)
//            } else {
//                mViewModel.requestChangeFollow(userInfoBean?.userId!!, 0)
//            }
//        }
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

        initRv()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun initScrollView() {
        mBinding.scrollView.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
            val location = IntArray(2)
            mBinding.ivAvatar.getLocationOnScreen(location)
            val locationY = location[1]
            var scale: Float = ((locationY - dp2px(85f).toFloat()) / dp2px(40f).toFloat()).toFloat()
            if (scale <= 0) {
                scale = 0f
                mBinding.ivBack.setImageResource(R.drawable.icon_back)
            } else {
                mBinding.ivBack.setImageResource(R.drawable.icon_back_white)
            }
            KLog.d("xujm", "locationY:$locationY===scale:$scale")
            val alpha = (255 * (1 - scale)).toInt()
//            mBinding.llTitle.background.alpha = (255 * (1 - scale)).toInt()
            mBinding.llTitle.setBackgroundColor(Color.argb(alpha, 255, 255, 255))
        }
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
                    setFocus()
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
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

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

        CoilUtils.loadCircle(mBinding.ivAvatar, userInfoBean?.avatar!!)
        mBinding.tvTitle.setText(userInfoBean?.userName)
        mBinding.tvName.setText(userInfoBean?.userName)
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
        } else {
            mBinding.tvAge.setText("$age  ${address}")
        }
        if (userInfoBean.gender == 1) {
            mBinding.tvAge.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.icon_home_boy,
                0,
                0,
                0
            )
            mBinding.tvAge.setTextColor(R.color._515FFF.getResColor())
            mBinding.tvAge.setShapeSolidColor(R.color._1A515FFF.getResColor())
        } else {
            mBinding.tvAge.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.icon_home_girl,
                0,
                0,
                0
            )
            mBinding.tvAge.setTextColor(R.color._FF517A.getResColor())
            mBinding.tvAge.setShapeSolidColor(R.color._1AFF517A.getResColor())
        }

        mBinding.tvState.setText(userInfoBean.recentDoing)
        mBinding.tvDesc.setText(userInfoBean.desc)
        if (userInfoBean.onlineStatus!!) {
            mBinding.ivOnline.visible()
        } else {
            mBinding.ivOnline.gone()
        }

        if (userInfoBean?.greeting?.duration ?: 0 == 0) {
            mBinding.tvVoice.gone()
        } else {
            mBinding.tvVoice.visible()
            val duration: Int = userInfoBean.greeting?.duration ?: 0
            mBinding.tvVoice.setText("${duration}s")
        }

        setFocus()
    }

    fun setFocus() {
//        if (userInfoBean?.followStatus == 0) {
//            mBinding.tvFocus.setText("关注")
//            mBinding.tvFocus.setShapeSolidColor(R.color._FF4F78.getResColor()).setUseShape()
//        } else if (userInfoBean?.followStatus == 1) {
//            mBinding.tvFocus.setText("已关注")
//            mBinding.tvFocus.setShapeSolidColor(R.color._A49389.getResColor()).setUseShape()
//        } else if (userInfoBean?.followStatus == 2) {
//            mBinding.tvFocus.setText("互相关注")
//            mBinding.tvFocus.setShapeSolidColor(R.color._A49389.getResColor()).setUseShape()
//        }
    }

    fun startVoicePlayAnimation(duration: Int) {
        countDownTimer = object : CountDownTimer(duration * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val second = millisUntilFinished / 1000
                mBinding.tvVoice.setText("${second}s")
            }

            override fun onFinish() {
                mBinding.tvVoice.setText("${duration}s")
            }
        }
        countDownTimer!!.start()
    }

    fun stopVoicePlayAnimation() {
        if (countDownTimer != null) {
            countDownTimer = null;
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.requestUserInfo(userId!!)
        requestRefresh()
    }


    fun initRv() {
        mAdapter = TaThinkAdapter()
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
}