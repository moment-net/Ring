package com.alan.module.main.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import com.alan.module.main.databinding.ActivityCreateNameBinding
import com.alan.module.main.viewmodel.CreateNameViewModel
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.TimePickerBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_CREATE)
@AndroidEntryPoint
class CreateNameActivity : BaseActivity<ActivityCreateNameBinding, CreateNameViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<CreateNameViewModel>()
    var birthday: String? = null

    /**
     * 初始化View
     */
    override fun ActivityCreateNameBinding.initView() {
        tvBirthdayValue.clickDelay {
            changeBirthday()
        }
        ivComplete.clickDelay {
            requestEditUserInfo()
        }


        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                tvLimit.setText("${s?.length!!}/15");
            }
        })

        if (SpHelper.getUserInfo() != null) {
            mBinding.etName.setText(SpHelper.getUserInfo()?.userName)
            mBinding.etName.setSelection(mBinding.etName.text.toString().length)
        }
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is UserInfoBean -> {
                    //用户信息更新
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

    }

    /**
     * 生日
     */
    fun changeBirthday() {
        //时间选择器
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        startDate[1938, 3] = 8
        val timePickerBuilder = TimePickerBuilder(this) { date: Date?, v: View? ->
            birthday = getTime(date)
            mBinding.tvBirthdayValue.setText(birthday)
        }
            .setRangDate(startDate, endDate)
            .setDate(getData(2020, 5, 4))

        if (!TextUtils.isEmpty(SpHelper.getUserInfo()?.birthday)) {
            val birth: List<String> = SpHelper.getUserInfo()?.birthday!!.split("-")
            val birthday = IntArray(3)
            if (birth.size == 3) {
                try {
                    for (i in birth.indices) {
                        birthday[i] = birth[i].toInt()
                    }
                    timePickerBuilder.setDate(getData(birthday[0], birthday[1] - 1, birthday[2]))
                } catch (e: java.lang.Exception) {
                    e.message
                }
            }
        } else {
            timePickerBuilder.setDate(getData(2000, 1, 1))
        }
        val pvTime = timePickerBuilder.build()
        pvTime.show()
    }

    fun getTime(date: Date?): String? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.YEAR].toString() + "-" + (calendar[Calendar.MONTH] + 1) + "-" + calendar[Calendar.DATE]
    }

    fun getData(year: Int, month: Int, day: Int): Calendar? {
        val calendar = Calendar.getInstance()
        calendar[year, month] = day
        return calendar
    }


    fun requestEditUserInfo() {
        val userName: String = mBinding.etName.getText().toString()
        if (TextUtils.isEmpty(userName)) {
            toast("昵称不能为空")
            return
        }

        mViewModel.requestEditUserInfo(
            userName,
            birthday ?: ""
        )
    }
}