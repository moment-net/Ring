package com.alan.module.home.activity

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alan.module.home.R
import com.alan.module.home.adapter.ManagerDetailTagAdapter
import com.alan.module.home.databinding.ActivityManagerInfoBinding
import com.alan.module.home.viewmodol.ManagerInfoViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
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

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<ManagerInfoViewModel>()

    /**
     * 初始化View
     */
    @SuppressLint("NewApi")
    override fun ActivityManagerInfoBinding.initView() {
        tvChat.clickDelay {
            jumpARoute(RouteUrl.ChatModule.ACTIVITY_CHAT_DETAIL)
        }
        initRVType()
        initRVSkil()
        initScrollView()
    }

    private fun initRVType() {
        var mTypeAdapter = ManagerDetailTagAdapter()
        mBinding.rvType.apply {
            addItemDecoration(
                MyColorDecoration(
                    0,
                    0,
                    context.dp2px(10f),
                    0,
                    ContextCompat.getColor(context, R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = mTypeAdapter
        }
    }

    private fun initRVSkil() {
        var mSkilAdapter = ManagerDetailTagAdapter()
        mBinding.rvSkil.apply {
            addItemDecoration(
                MyColorDecoration(
                    0,
                    0,
                    context.dp2px(10f),
                    0,
                    ContextCompat.getColor(context, R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = mSkilAdapter
        }
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

    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }
}