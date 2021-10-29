package com.alan.module.my.activity

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.alan.module.my.R
import com.alan.module.my.adapter.SelectCardAdapter
import com.alan.module.my.databinding.ActivitySelectCardBinding
import com.alan.module.my.viewmodol.SelectCardViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.NowTagBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.common.constant.RouteUrl
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
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_CARD)
@AndroidEntryPoint
class SelectCardActivity : BaseActivity<ActivitySelectCardBinding, SelectCardViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<SelectCardViewModel>()
    lateinit var mAdapter: SelectCardAdapter

    /**
     * 初始化View
     */
    override fun ActivitySelectCardBinding.initView() {
        ivBack.clickDelay { finish() }

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
        mAdapter = SelectCardAdapter()
        mBinding.rvList.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, dp2px(10f), dp2px(10f),
                    ContextCompat.getColor(context, R.color.white)
                )
            )
            layoutManager =
                FlexboxLayoutManager(this@SelectCardActivity, FlexDirection.ROW, FlexWrap.WRAP)
            adapter = mAdapter
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_label_bg -> {
                    mAdapter.selectPosition = position
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}