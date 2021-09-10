package com.alan.module.home.activity

import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ImageSpan
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.alan.module.home.R
import com.alan.module.home.databinding.ActivityManagerInfoBinding
import com.alan.module.home.viewmodol.ManagerInfoViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.CookerBean
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.socks.library.KLog
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.HomeModule.ACTIVITY_HOME_MANAGER)
@AndroidEntryPoint
class ManagerInfoActivity : BaseActivity<ActivityManagerInfoBinding, ManagerInfoViewModel>() {

    @JvmField
    @Autowired(name = "bean")
    var cookerBean: CookerBean? = null

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
        tvFocus.clickDelay {
            if (cookerBean?.user?.followStatus == 0) {
                mViewModel.requestChangeFollow(cookerBean?.user?.userId!!, 1)
            } else {
                mViewModel.requestChangeFollow(cookerBean?.user?.userId!!, 0)
            }
        }
        tvChat.clickDelay {
            val bundle = Bundle().apply {
                putString("userId", cookerBean!!.user.userId)
                putString("userName", cookerBean!!.user.userName)
                putString("avatar", cookerBean!!.user.avatar)
            }
            jumpARoute(RouteUrl.ChatModule.ACTIVITY_CHAT_DETAIL, bundle)
        }
        initScrollView()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun initScrollView() {
        mBinding.scrollView.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
            val location = IntArray(2)
            mBinding.ivAvatar.getLocationOnScreen(location)
            val locationY = location[1]
            var scale: Float = ((locationY - dp2px(52f).toFloat()) / dp2px(40f).toFloat()).toFloat()
            if (scale <= 0) {
                scale = 0f
                mBinding.ivBack.setImageResource(R.drawable.icon_back)
            } else {
                mBinding.ivBack.setImageResource(R.drawable.icon_back_white)
            }
            KLog.d("xujm", "locationY:$locationY===scale:$scale")
            mBinding.llTitle.background.alpha = (255 * (1 - scale)).toInt();
        }
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            cookerBean?.user?.followStatus = it
            setFocus()
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {
        setUserInfo()
    }

    fun setUserInfo() {
        if (cookerBean == null) {
            return
        }
        val userInfoBean = cookerBean?.user;
        CoilUtils.loadRound(mBinding.ivAvatar, userInfoBean?.avatar!!, 3f)
        CoilUtils.loadBlur(mBinding.ivAvatarBg, userInfoBean?.avatar!!, this, 25f, 1f)
        mBinding.tvName.setText(userInfoBean?.userName)
        var address = ""
        if (!TextUtils.isEmpty(cookerBean?.user?.address) && cookerBean?.user?.address!!.split("-").size == 3) {
            address = cookerBean?.user?.address!!.split("-")[2]
        }
        mBinding.tvAge.setText("${userInfoBean?.age}岁  ${address}")
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

        mBinding.tvDeclaration.setText(userInfoBean.desc)
        mBinding.tvState.setText(cookerBean?.title)
        if (userInfoBean.onlineStatus!!) {
            mBinding.ivOnline.visible()
        } else {
            mBinding.ivOnline.gone()
        }

        var duration: Int = (userInfoBean?.greeting?.duration ?: 0) / 1000
        mBinding.tvVoice.text = ("${duration}s")


        var tagList = cookerBean?.tag
        if (tagList != null && !tagList.isEmpty()) {
            mBinding.tvTag.visible()
            var stringBuilder = StringBuilder()
            var list = arrayListOf<Int>()
            for (position in 0..tagList.size - 1) {
                if (position < tagList.size - 1) {
                    stringBuilder.append("${tagList.get(position)} ")
                    list.add(stringBuilder.length - 1)
                } else {
                    stringBuilder.append("${tagList.get(position)}")
                }
            }
            var spannableString = SpannableString(stringBuilder.toString());
            for (index in list) {
                val imageSpan = ImageSpan(this, R.drawable.icon_home_line)
                spannableString.setSpan(
                    imageSpan,
                    index,
                    index + 1,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                );
            }
            mBinding.tvTag.setText(spannableString)
        } else {
            mBinding.tvTag.gone()
        }

        var likeList = cookerBean?.likes
        if (likeList != null && !likeList.isEmpty()) {
            mBinding.tvLike.visible()
            var stringBuilder = StringBuilder()
            var list = arrayListOf<Int>()
            for (position in 0..likeList.size - 1) {
                if (position < likeList.size - 1) {
                    stringBuilder.append("${likeList.get(position)} ")
                    list.add(stringBuilder.length - 1)
                } else {
                    stringBuilder.append("${likeList.get(position)}")
                }
            }
            var spannableString = SpannableString(stringBuilder.toString());

            for (index in list) {
                val imageSpan = ImageSpan(this, R.drawable.icon_home_line)
                spannableString.setSpan(
                    imageSpan,
                    index,
                    index + 1,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                );
            }
            mBinding.tvLike.setText(spannableString)
        } else {
            mBinding.tvLike.gone()
        }
        setFocus()
    }

    fun setFocus() {
        if (cookerBean?.user?.followStatus == 0) {
            mBinding.tvFocus.setText("关注")
            mBinding.tvFocus.setShapeSolidColor(R.color._FF4F78.getResColor()).setUseShape()
        } else if (cookerBean?.user?.followStatus == 1) {
            mBinding.tvFocus.setText("已关注")
            mBinding.tvFocus.setShapeSolidColor(R.color._A49389.getResColor()).setUseShape()
        } else if (cookerBean?.user?.followStatus == 2) {
            mBinding.tvFocus.setText("互相关注")
            mBinding.tvFocus.setShapeSolidColor(R.color._A49389.getResColor()).setUseShape()
        }
    }

}