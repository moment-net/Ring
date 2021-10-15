package com.alan.module.main.activity

import androidx.activity.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.ActivityMyBinding
import com.alan.module.main.fragment.MyFragment
import com.alan.module.main.viewmodel.MyViewModel
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：我的页面
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_MY)
@AndroidEntryPoint
class MyActivity : BaseActivity<ActivityMyBinding, MyViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<MyViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityMyBinding.initView() {
        val fragment = MyFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl_content, fragment).commitAllowingStateLoss()
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