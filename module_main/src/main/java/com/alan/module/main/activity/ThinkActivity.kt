package com.alan.module.main.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.activity.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.ActivityThinkBinding
import com.alan.module.main.viewmodel.PushThinkViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.ktx.getResDrawable
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.ChangeThinkEvent
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：发布想法
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_THINK)
@AndroidEntryPoint
class ThinkActivity : BaseActivity<ActivityThinkBinding, PushThinkViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<PushThinkViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityThinkBinding.initView() {
        ivBack.clickDelay { finish() }
        tvRight.clickDelay {
            val content = etContent.text.toString()
            if (TextUtils.isEmpty(content)) {
                toast("请输入您的想法")
                return@clickDelay
            }
            mViewModel.requestPushThink(content)
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
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is Boolean -> {
                    toast("发布成功")
                    EventBusUtils.postEvent(ChangeThinkEvent(1))
                    finish()
                }
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

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

    override fun onResume() {
        super.onResume()
        mBinding.etContent.isFocusable = true
        mBinding.etContent.isFocusableInTouchMode = true
        mBinding.etContent.requestFocus()
    }

}