package com.alan.module.main.activity

import android.text.TextUtils
import androidx.activity.viewModels
import com.alan.module.main.R
import com.alan.module.main.adapter.NowLabelAdapter
import com.alan.module.main.databinding.ActivitySelectStateBinding
import com.alan.module.main.viewmodel.StateViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.NowTagBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.report.DataPointUtil
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_STATE)
@AndroidEntryPoint
class SelectStateActivity : BaseActivity<ActivitySelectStateBinding, StateViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<StateViewModel>()
    lateinit var mAdapter: NowLabelAdapter
    var tag: String = ""
    var defaultText: String = ""

    /**
     * 初始化View
     */
    override fun ActivitySelectStateBinding.initView() {
        tvJump.clickDelay {
            DataPointUtil.reportClickSkipStatus(SpHelper.getUserInfo()?.userId!!)
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MAIN)
            finish()
        }
        tvNext.clickDelay {
            if (TextUtils.isEmpty(tag)) {
                toast("请选择")
                return@clickDelay
            }
            mViewModel.requestPushNow(tag, defaultText)
            DataPointUtil.reportClickStatus(SpHelper.getUserInfo()?.userId!!, tag)
        }

        initRv()
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    val list = it.data as ArrayList<NowTagBean>

                    mAdapter.setList(list)
                }

                is Boolean -> {
                    toast("发布成功")
                    jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MAIN)
                    finish()
                }
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {
        mViewModel.requestNowTagList()
    }

    fun initRv() {
        mAdapter = NowLabelAdapter()
        mBinding.rvList.apply {
            layoutManager =
                FlexboxLayoutManager(this@SelectStateActivity, FlexDirection.ROW, FlexWrap.WRAP)
            adapter = mAdapter
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_label_bg -> {
                    mAdapter.selectPosition = position
                    mAdapter.notifyDataSetChanged()
                    tag = mAdapter.data.get(position).tag
                    defaultText = mAdapter.data.get(position).defaultText
                }
            }
        }
    }
}