package com.alan.module.main.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.alan.module.main.R
import com.alan.module.main.databinding.LayoutInputBinding
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import com.alan.mvvm.base.utils.jumpARoute
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InputFragmentDialog : BaseFrameDialogFragment<LayoutInputBinding, EmptyViewModel>() {

    companion object {
        fun newInstance(userId: String, userName: String): InputFragmentDialog {
            val bundle = Bundle()
            bundle.putString("userId", userId)
            bundle.putString("userName", userName)
            val dialog = InputFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
        }
    }

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()
    lateinit var userId: String
    lateinit var userName: String

    override fun initWindow() {
        setDimAmount(0f)
        super.initWindow()
        val window = dialog!!.window
        val params = window!!.attributes
        val colorDrawable = ColorDrawable(
            ContextCompat.getColor(
                mActivity, R.color.transparent
            )
        )
        window.setBackgroundDrawable(colorDrawable)
        params.width = dp2px(360f)
        params.height = dp2px(51f)
        params.gravity = Gravity.BOTTOM
        setCanceledOnTouchOutside(true)
        isCancelable = true
        window.attributes = params
    }


    override fun LayoutInputBinding.initView() {
        arguments?.apply {
            userId = getString("userId", "")
            userName = getString("userName", "")
        }
        mBinding.etReply.setHint("回复${userName}：")

        mBinding.tvSend.clickDelay {
            val content = etReply.text.toString()
            if (TextUtils.isEmpty(content)) {
                toast("请输入回复内容")
                return@clickDelay
            }
            val bundle = Bundle().apply {
                putString("userId", userId)
                putString("content", content)
            }
            jumpARoute(RouteUrl.ChatModule.ACTIVITY_CHAT_DETAIL, bundle)
            dismiss()
        }
    }


    override fun initObserve() {
    }

    override fun initRequestData() {

    }

    override fun onResume() {
        super.onResume()
        mBinding.etReply.postDelayed(Runnable {
            showSoftInputFromWindow(mBinding.etReply)
        }, 50)
    }

    /**
     * EditText获取焦点弹出软键盘
     */
    fun showSoftInputFromWindow(editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        val inputManager: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(editText, 0)
    }


}