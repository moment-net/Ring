package com.alan.module.main.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.module.main.R
import com.alan.module.main.adapter.StateAdapter
import com.alan.module.main.databinding.LayoutDialogStateBinding
import com.alan.module.main.viewmodel.StateViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.MealStateBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.common.dialog.DialogHelper
import com.alan.mvvm.common.event.RefreshEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StateFragmentDialog : BaseFrameDialogFragment<LayoutDialogStateBinding, StateViewModel>() {

    companion object {
        fun newInstance(): StateFragmentDialog {
            val bundle = Bundle()
            val dialog = StateFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
        }
    }

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<StateViewModel>()
    private lateinit var mAdapter: StateAdapter
    var countDownTimer: CountDownTimer? = null
    var mealState: Int = 0

    override fun LayoutDialogStateBinding.initView() {
        ivClose.clickDelay {
            mViewModel.requestEditMeal("$mealState")
        }
        ivQa.clickDelay {
            showPopupWindow()
        }
        tvStop.clickDelay {
            showStopDialog()
        }
        initRV()
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
                    mealState = position + 1
                    mAdapter.selectPositon = position
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
    }


    override fun initWindow() {
        super.initWindow()
        val window = dialog!!.window
        val params = window!!.attributes
        val colorDrawable = ColorDrawable(
            ContextCompat.getColor(
                mActivity, R.color.transparent
            )
        )
        window.setBackgroundDrawable(colorDrawable)
        params.width = dp2px(320f)
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(true)
        isCancelable = true
        window.attributes = params
    }

    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    if (it.data != null) {
                        val stateBean = it.data as MealStateBean
                        mealState = stateBean.value
                        mAdapter.selectPositon = mealState - 1
                        mAdapter.notifyDataSetChanged()
                        startCountDown(stateBean.endTime)
                    }
                }
                is Boolean -> {
                    if (it) {
                        EventBusUtils.postEvent(RefreshEvent("main"))
                        dismiss()
                    }
                }
            }
        }
    }

    override fun initRequestData() {
        mViewModel.requestMealStatus()
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
                mViewModel.requestMealStop()
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


    fun showStopDialog() {
        DialogHelper.showMultipleDialog(mActivity, "停止匹配将不会给你匹配新的饭友", "取消", "确定", {

        }, {
            mViewModel.requestMealStop()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null) {
            countDownTimer!!.cancel();
            countDownTimer = null;
        }
    }

}