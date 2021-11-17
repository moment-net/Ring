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
import com.alan.module.main.databinding.LayoutDialogCreateTagBinding
import com.alan.module.main.viewmodel.CreateTagViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreateTagFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogCreateTagBinding, CreateTagViewModel>() {

    companion object {
        fun newInstance(): CreateTagFragmentDialog {
            val bundle = Bundle()
            val dialog = CreateTagFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
        }
    }

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<CreateTagViewModel>()
    lateinit var userId: String
    lateinit var userName: String
    lateinit var listener: OnCompleteListener

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


    override fun LayoutDialogCreateTagBinding.initView() {
        mBinding.tvSend.clickDelay {
            val content = etReply.text.toString()
            if (TextUtils.isEmpty(content)) {
                toast("请输入标签内容")
                return@clickDelay
            }
            if (content.length < 2) {
                toast("请输入标签内容2-10个字")
                return@clickDelay
            }
            mViewModel.requestModifyTag(content)
        }
    }


    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is Boolean -> {
                    toast("添加成功")
                    if (listener != null) {
                        listener.onComplete()
                    }
                    dismiss()
                }
            }
        }
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

    interface OnCompleteListener {
        fun onComplete()
    }

}