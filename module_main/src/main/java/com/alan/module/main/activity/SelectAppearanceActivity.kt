package com.alan.module.main.activity

import android.animation.ValueAnimator
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.module.main.R
import com.alan.module.main.adapter.OptionAdapter
import com.alan.module.main.databinding.ActivitySelectAppearanceBinding
import com.alan.module.main.viewmodel.SelectAppearanceViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.AppearanceListBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
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
 * 备注：选择形象
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_APPEARANCE)
@AndroidEntryPoint
class SelectAppearanceActivity :
    BaseActivity<ActivitySelectAppearanceBinding, SelectAppearanceViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<SelectAppearanceViewModel>()
    var isBoy: Boolean = true
    var bean: AppearanceListBean? = null
    var currentName: String = ""
    var currentBoyPosition: Int = 0
    var currentGirlPosition: Int = 0
    var maxBoyHeight: Int = 0
    var maxGirlHeight: Int = 0
    var boyAdapter: OptionAdapter? = null
    var girlAdapter: OptionAdapter? = null

    /**
     * 初始化View
     */
    override fun ActivitySelectAppearanceBinding.initView() {
        clBoy.clickDelay {
            if (!isBoy) {
                isBoy = true
                translate(clBoy, true)
                translate(clGirl, false)
                val bean = boyAdapter?.data?.get(currentBoyPosition)
                currentName = bean?.name!!
                CoilUtils.load(mBinding.ivPic, bean.url)
            }
        }
        clGirl.clickDelay {
            if (isBoy) {
                isBoy = false
                translate(clBoy, false)
                translate(clGirl, true)
                val bean = girlAdapter?.data?.get(currentGirlPosition)
                currentName = bean?.name!!
                CoilUtils.load(mBinding.ivPic, bean.url)
            }
        }

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
                    bean = it
                    val boy = bean?.boy
                    val girl = bean?.girl
                    boyAdapter?.setList(boy)
                    girlAdapter?.setList(girl)
                    maxBoyHeight = (boy?.size!! + 1) * dp2px(46f)
                    maxGirlHeight = (girl?.size!! + 1) * dp2px(46f)

                    currentBoyPosition = 0
                    currentName = boy?.get(currentBoyPosition).name
                    CoilUtils.load(mBinding.ivPic, boy?.get(currentBoyPosition).url)
                }

                is UserInfoBean -> {
                    finish()
                    jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_CREATE)
                }
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {
        mViewModel.requestList()
    }

    /**
     * 动画
     */
    fun translate(cl: ConstraintLayout, isOpen: Boolean) {
        val maxHeight = if (isBoy) {
            maxBoyHeight
        } else {
            maxGirlHeight
        }
        KLog.e("xujm", "maxHeight:$maxHeight")
        var start = 0
        var end = 0
        if (isOpen) {
            start = dp2px(46f)
            end = maxHeight
        } else {
            start = maxHeight
            end = dp2px(46f)
        }
        val animator = ValueAnimator.ofInt(start, end)
        animator.addUpdateListener {
            val animatedValue = it.animatedValue as Int
            val layoutParams = cl.layoutParams
            layoutParams.height = animatedValue
            cl.layoutParams = layoutParams
        }
        animator.setDuration(500)
        animator.start()
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
            layoutManager = LinearLayoutManager(this@SelectAppearanceActivity)
            adapter = boyAdapter
        }

        boyAdapter?.setOnItemClickListener { adapter, view, position ->
            boyAdapter?.currentCheck = position
            boyAdapter?.notifyDataSetChanged()

            val bean = boyAdapter?.data?.get(position)
            CoilUtils.load(mBinding.ivPic, bean?.url!!)
            currentBoyPosition = position
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
            layoutManager = LinearLayoutManager(this@SelectAppearanceActivity)
            adapter = girlAdapter
        }

        girlAdapter?.setOnItemClickListener { adapter, view, position ->
            girlAdapter?.currentCheck = position
            girlAdapter?.notifyDataSetChanged()

            val bean = girlAdapter?.data?.get(position)
            CoilUtils.load(mBinding.ivPic, bean?.url!!)
            currentGirlPosition = position
            currentName = bean.name
        }
    }
}