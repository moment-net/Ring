package com.alan.module.main.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.alan.module.main.R
import com.alan.module.main.adapter.NowLabelAdapter
import com.alan.module.main.databinding.ActivityNowBinding
import com.alan.module.main.viewmodel.PushNowViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.NowTagBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.ktx.getResDrawable
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.ChangeThinkEvent
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：发布现在
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_NOW)
@AndroidEntryPoint
class NowActivity : BaseActivity<ActivityNowBinding, PushNowViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<PushNowViewModel>()
    lateinit var mAdapter: NowLabelAdapter
    var tag: String = ""

    /**
     * 初始化View
     */
    override fun ActivityNowBinding.initView() {
        ivBack.clickDelay { finish() }
        tvRight.clickDelay {
            val content = etContent.text.toString()
            if (TextUtils.isEmpty(content)) {
                toast("请输入您的想法")
                return@clickDelay
            }
            if (TextUtils.isEmpty(tag)) {
                tag = "正在"
            }
            mViewModel.requestPushNow(tag, content)
        }

        etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length!! > 0) {
                    changeBt(true)
                } else {
                    changeBt(false)
                }

                tvLimit.setText("${s?.length!!}/500");
            }
        })

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
                    EventBusUtils.postEvent(ChangeThinkEvent(0))
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

    fun changeBt(enable: Boolean) {
        if (enable) {
            mBinding.tvRight.isEnabled = true
            mBinding.tvRight.setTextColor(R.color.white.getResColor())
            mBinding.tvRight.background = R.drawable.icon_push_bt_enable.getResDrawable()
        } else {
            mBinding.tvRight.isEnabled = false
            mBinding.tvRight.setTextColor(R.color._80393939.getResColor())
            mBinding.tvRight.background = R.drawable.icon_push_bt_disenable.getResDrawable()
        }
    }

    fun initRv() {
        mAdapter = NowLabelAdapter()
        mBinding.rvList.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, dp2px(10f), dp2px(10f),
                    ContextCompat.getColor(context, R.color.white)
                )
            )
            layoutManager = FlexboxLayoutManager(this@NowActivity, FlexDirection.ROW, FlexWrap.WRAP)
            adapter = mAdapter
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_label_bg -> {
                    mAdapter.selectPosition = position
                    mAdapter.notifyDataSetChanged()
                    tag = mAdapter.data.get(position).tag
                    mBinding.etContent.setText(mAdapter.data.get(position).defaultText)
                    mBinding.etContent.setSelection(mBinding.etContent.text.length)

//                    val inputDialog = InputFragmentDialog.newInstance(userId, userName)
//                    inputDialog.show(requireActivity().supportFragmentManager)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.etContent.isFocusable = true
        mBinding.etContent.isFocusableInTouchMode = true
        mBinding.etContent.requestFocus()
    }
}