package com.alan.module.main.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.module.main.R
import com.alan.module.main.adapter.StateAdapter
import com.alan.module.main.databinding.FragmentSelectStateBinding
import com.alan.module.main.viewmodel.SelectStateViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.MatchTimeBean
import com.alan.mvvm.base.http.responsebean.MealStateBean
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.common.dialog.DialogHelper
import com.alan.mvvm.common.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：匹配设置页面
 */
@AndroidEntryPoint
class SelectStateFragment : BaseFragment<FragmentSelectStateBinding, SelectStateViewModel>() {

    override val mViewModel by viewModels<SelectStateViewModel>()
    private lateinit var mAdapter: StateAdapter
    var countDownTimer: CountDownTimer? = null
    var mealState: Int = 0
    var listener: OnStateClickListener? = null
    lateinit var stateBean: MealStateBean

    companion object {
        @JvmStatic
        fun newInstance() =
            SelectStateFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun FragmentSelectStateBinding.initView() {
        ivQa.clickDelay {
            showPopupWindow()
        }
        tvSet.clickDelay {
            listener?.onClickSet()
        }
        tvStop.clickDelay {
            if (stateBean.matchStatus) {
                showStopDialog()
            } else {
                mViewModel.requestMealStop("1")
            }
        }
        tvCommit.clickDelay {
            listener?.onClickCommit()
        }
        initRV()
    }

    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is MealStateBean -> {
                    stateBean = it
                    if (stateBean.inMatchTime) {
                        mBinding.rvState.visible()
                        mBinding.tvStop.visible()
                        mBinding.tvContent.gone()
                        mBinding.tvCommit.gone()

                        mBinding.tvInfo.setText(stateBean.title)
                        startCountDown(stateBean.currentEndTime)

                        if (stateBean.matchStatus) {
                            mBinding.tvStop.setText("停止匹配")
                            mBinding.tvStop.setTextColor(R.color._803A3A3A.getResColor())
                            mealState = stateBean.value
                            mAdapter.selectPositon = mealState - 1
                            mAdapter.notifyDataSetChanged()
                        } else {
                            mBinding.tvStop.setText("开始匹配")
                            mBinding.tvStop.setTextColor(R.color._3A3A3A.getResColor())
                        }

                    } else {
                        mBinding.rvState.gone()
                        mBinding.tvStop.gone()
                        mBinding.tvContent.visible()
                        mBinding.tvCommit.visible()

                        mBinding.tvInfo.setText(stateBean.title)
                        startCountDown(stateBean.nextBeginTime)
                    }
                }
                is BaseResponse<*> -> {
                    val arrayList = it.data as ArrayList<MatchTimeBean>
                    val stringBuilder = StringBuilder()
                    if (!arrayList.isEmpty()) {
                        for (bean in arrayList) {
                            val time = "${bean.name} ${bean.beginTime}-${bean.endTime}\n"
                            stringBuilder.append(time)
                        }
                    }
                    mBinding.tvContent.setText("以下饭点时间才可以匹配哦！\n饭点时间：\n${stringBuilder.toString()}")
                }
                is Boolean -> {
                    //修改干饭状态

                }
                is String -> {
                    //开启或关闭匹配
                    if (it.equals("0")) {
                        mBinding.tvStop.setText("停止匹配")
                        mBinding.tvStop.setTextColor(R.color._803A3A3A.getResColor())
                    } else {
                        mBinding.tvStop.setText("开始匹配")
                        mBinding.tvStop.setTextColor(R.color._3A3A3A.getResColor())
                    }
                }
            }
        }
    }

    override fun initRequestData() {
        mViewModel.requestMealStatus()
        mViewModel.requestMatchTime()
    }

    private fun initRV() {
        mAdapter = StateAdapter()
        mBinding.rvState.apply {
            addItemDecoration(
                MyColorDecoration(
                    bottom = dp2px(15f),
                    color = ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        /**
        (1, "焦急等待外卖中"),
        (2, "正在干饭，欢迎来撩"),
        (3, "饭后甜点中"),
        (4, "饭后思考人生中"),
        (5, "饭后葛优躺中"),
        (6, "不知道下顿吃什么");
         */
        val mList = arrayListOf<String>()
        mList.add("焦急等待外卖")
        mList.add("正在干饭，欢迎来撩")
        mList.add("饭后甜点中")
        mList.add("饭后思考人生中")
        mList.add("饭后葛优躺中")
        mList.add("不知道下顿吃什么")
        mAdapter.setList(mList)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.ctv_label -> {
                    if (stateBean.matchStatus) {
                        mealState = position + 1
                        mAdapter.selectPositon = position
                        mAdapter.notifyDataSetChanged()
                        mViewModel.requestEditMeal("$mealState")
                    } else {
                        showStartDialog(position)
                    }
                }
            }
        }
    }

    fun startCountDown(endTime: Long) {
        val timeLong = endTime * 1000 - System.currentTimeMillis()
        countDownTimer = object : CountDownTimer(timeLong, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //单位时
                val hour: Long = millisUntilFinished / (1000 * 60 * 60)
                //单位分
                val minute: Long = (millisUntilFinished - hour * (1000 * 60 * 60)) / (1000 * 60)
                //单位秒
                val second: Long =
                    (millisUntilFinished - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000
                val hourString = if (hour < 10) "0$hour" else "$hour"
                val minuteString = if (minute < 10) "0$minute" else "$minute"
                val secondString = if (second < 10) "0$second" else "$second"
                mBinding.tvTime.setText("$hourString:$minuteString:$secondString")
            }

            override fun onFinish() {
                mViewModel.requestMealStatus()
            }
        }
        countDownTimer!!.start()
    }


    /**
     * 显示菜单项
     */
    fun showPopupWindow() {
        val contentview: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.layout_tips, null)
        contentview.isFocusable = true
        contentview.isFocusableInTouchMode = true

        val popupWindow = PopupWindow(
            contentview,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setFocusable(true)
        popupWindow.setOutsideTouchable(false)
        popupWindow.showAsDropDown(mBinding.ivQa, -dp2px(135f), dp2px(5f))
    }

    fun showStartDialog(position: Int) {
        DialogHelper.showMultipleDialog(requireActivity(), "开启匹配后才可以选择状态，是否要开启匹配？", "开启", "取消", {
            mealState = position + 1
            mAdapter.selectPositon = position
            mAdapter.notifyDataSetChanged()
            mViewModel.requestEditMeal("$mealState")
            mViewModel.requestMealStop("1")
        }, {

        })
    }

    fun showStopDialog() {
        DialogHelper.showMultipleDialog(requireActivity(), "停止匹配将不会给你匹配新的饭友", "取消", "确定", {

        }, {
            mViewModel.requestMealStop("0")
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null) {
            countDownTimer!!.cancel();
            countDownTimer = null;
        }
    }


    interface OnStateClickListener {
        fun onClickSet()
        fun onClickCommit()
    }
}