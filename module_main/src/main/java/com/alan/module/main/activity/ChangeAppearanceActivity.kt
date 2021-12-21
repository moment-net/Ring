package com.alan.module.main.activity

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.module.main.R
import com.alan.module.main.adapter.OptionAdapter
import com.alan.module.main.databinding.ActivityChangeAppearanceBinding
import com.alan.module.main.viewmodel.ChangeAppearanceViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.AppearanceListBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_CHANGEAPPEARANCE)
@AndroidEntryPoint
class ChangeAppearanceActivity :
    BaseActivity<ActivityChangeAppearanceBinding, ChangeAppearanceViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<ChangeAppearanceViewModel>()
    var boyAdapter: OptionAdapter? = null
    var girlAdapter: OptionAdapter? = null

    var currentName: String = ""
    var isBoy: Boolean = true

    /**
     * 初始化View
     */
    override fun ActivityChangeAppearanceBinding.initView() {
        ivBack.clickDelay { finish() }

        ivNext.clickDelay {
            mViewModel.requestModelSet(currentName)
        }

        initRvBoy()
        initRvGirl()
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is AppearanceListBean -> {
                    val boy = it.boy
                    val girl = it.girl
                    boyAdapter?.setList(boy)
                    girlAdapter?.setList(girl)

                    if (isBoy) {
                        val model = SpHelper.getUserInfo()?.model
                        val position = boy.indexOf(model)
                        boyAdapter?.currentCheck = position
                        boyAdapter?.notifyDataSetChanged()
                        CoilUtils.load(mBinding.ivPic, model?.url!!)
                        currentName = model.name
                    } else {
                        val model = SpHelper.getUserInfo()?.model
                        val position = girl.indexOf(model)
                        girlAdapter?.currentCheck = position
                        girlAdapter?.notifyDataSetChanged()
                        CoilUtils.load(mBinding.ivPic, model?.url!!)
                        currentName = model.name
                    }
                }

                is UserInfoBean -> {
                    finish()
                }
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {
        val userInfo = SpHelper.getUserInfo()
        val gender = userInfo?.gender
        if (gender == 1) {
            isBoy = true
            mBinding.clBoy.visible()
            mBinding.clGirl.gone()
        } else {
            isBoy = false
            mBinding.clBoy.gone()
            mBinding.clGirl.visible()
        }

        mViewModel.requestList()
    }

    fun initRvBoy() {
        boyAdapter = OptionAdapter(1)
        mBinding.rvBoy.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0,
                    dp2px(10f),
                    ContextCompat.getColor(context, R.color.transparent)
                )
            )
            layoutManager = LinearLayoutManager(this@ChangeAppearanceActivity)
            adapter = boyAdapter
        }

        boyAdapter?.setOnItemClickListener { adapter, view, position ->
            boyAdapter?.currentCheck = position
            boyAdapter?.notifyDataSetChanged()

            val bean = boyAdapter?.data?.get(position)
            CoilUtils.load(mBinding.ivPic, bean?.url!!)
            currentName = bean.name
        }
    }

    fun initRvGirl() {
        girlAdapter = OptionAdapter(2)
        mBinding.rvGirl.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0,
                    dp2px(10f),
                    ContextCompat.getColor(context, R.color.transparent)
                )
            )
            layoutManager = LinearLayoutManager(this@ChangeAppearanceActivity)
            adapter = girlAdapter
        }

        girlAdapter?.setOnItemClickListener { adapter, view, position ->
            girlAdapter?.currentCheck = position
            girlAdapter?.notifyDataSetChanged()

            val bean = girlAdapter?.data?.get(position)
            CoilUtils.load(mBinding.ivPic, bean?.url!!)
            currentName = bean.name
        }
    }
}