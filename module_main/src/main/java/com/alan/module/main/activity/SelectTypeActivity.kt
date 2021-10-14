package com.alan.module.main.activity

import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.alan.module.main.R
import com.alan.module.main.databinding.ActivitySelectTypeBinding
import com.alan.module.main.viewmodel.SelectTypeViewModel
import com.alan.mvvm.base.http.responsebean.TargetBean
import com.alan.mvvm.base.http.responsebean.TargetInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import leifu.shapelibrary.ShapeView

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_TYPE)
@AndroidEntryPoint
class SelectTypeActivity : BaseActivity<ActivitySelectTypeBinding, SelectTypeViewModel>() {
    var viewList = arrayListOf<ShapeView>()
    var allList = arrayListOf<String>()
    var targetList = arrayListOf<String>()

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<SelectTypeViewModel>()

    /**
     * 初始化View
     */
    override fun ActivitySelectTypeBinding.initView() {
        tvJump.clickDelay { jumpNext() }
        tvNext.clickDelay {
            if (targetList.size == 0) {
                toast("请选择你的干饭标签")
                return@clickDelay
            }
            mViewModel.requestSaveTarget(targetList)
        }
        tvOne.clickDelay {
            check(0, tvOne)
        }
        tvTwo.clickDelay {
            check(1, tvTwo)
        }
        tvThree.clickDelay {
            check(2, tvThree)
        }
        tvFour.clickDelay {
            check(3, tvFour)
        }
        tvFive.clickDelay {
            check(4, tvFive)
        }
        tvSix.clickDelay {
            check(5, tvSix)
        }
        tvSeven.clickDelay {
            check(6, tvSeven)
        }
        tvEight.clickDelay {
            check(7, tvEight)
        }
        tvNine.clickDelay {
            check(8, tvNine)
        }
        tvTen.clickDelay {
            check(9, tvTen)
        }
        tvEleven.clickDelay {
            check(10, tvEleven)
        }

        viewList.add(tvOne)
        viewList.add(tvTwo)
        viewList.add(tvThree)
        viewList.add(tvFour)
        viewList.add(tvFive)
        viewList.add(tvSix)
        viewList.add(tvSeven)
        viewList.add(tvEight)
        viewList.add(tvNine)
        viewList.add(tvTen)
        viewList.add(tvEleven)
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is TargetBean -> {
                    allList = it.type!!
                    for (index in 0..allList.size - 1) {
                        viewList.get(index).setText(allList.get(index))
                        viewList.get(index).visibility = View.VISIBLE
                    }
                }
                is TargetInfoBean -> {
                    jumpNext()
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


    fun jumpNext() {
        finish()
        jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_MAIN)
    }


    fun check(positon: Int, tv: ShapeView) {
        if (targetList.contains(allList.get(positon))) {
            tv.setTextColor(R.color.black.getResColor())
            tv.setShapeSolidColor(ContextCompat.getColor(this, R.color._083A3A3A)).setUseShape()
            targetList.remove(allList.get(positon))
        } else {
            if (targetList.size == 3) {
                toast("已经达到上限")
                return
            }
            tv.setTextColor(R.color.white.getResColor())
            tv.setShapeSolidColor(ContextCompat.getColor(this, R.color._FFBD2A)).setUseShape()
            targetList.add(allList.get(positon))
        }
    }
}