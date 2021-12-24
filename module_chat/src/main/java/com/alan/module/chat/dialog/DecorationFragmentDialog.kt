package com.alan.module.chat.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.alan.module.chat.R
import com.alan.module.chat.adapter.DecorationAdapter
import com.alan.module.chat.databinding.LayoutDecorationListBinding
import com.alan.module.chat.viewmodol.DecorationViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.ChatBgBean
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.base.utils.MyGridItemDecoration
import com.alan.mvvm.common.event.DecorationEvent
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/8/4
 * 备注：聘请弹框
 */
@AndroidEntryPoint
class DecorationFragmentDialog :
    BaseFrameDialogFragment<LayoutDecorationListBinding, DecorationViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<DecorationViewModel>()
    private lateinit var mAdapter: DecorationAdapter
    private lateinit var userId: String


    companion object {
        fun newInstance(userId: String): DecorationFragmentDialog {
            val bundle = Bundle()
            bundle.putString("userId", userId)
            val dialog = DecorationFragmentDialog()
            dialog.setArguments(bundle)
            return dialog
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
        params.width = dp2px(280f)
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(true)
        isCancelable = true
        window.attributes = params
    }


    override fun LayoutDecorationListBinding.initView() {
        arguments?.apply {
            userId = getString("userId")!!
        }
        initRV()
    }

    private fun initRV() {
        mAdapter = DecorationAdapter()
        mBinding.rvList.apply {
            addItemDecoration(
                MyGridItemDecoration(
                    dp2px(15f),
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_content -> {
                    val list = mAdapter.data
                    for (bean in list) {
                        bean.selected = false
                    }
                    list.get(position).selected = true
                    mAdapter.notifyDataSetChanged()

                    val name = list.get(position).name
                    mViewModel.requestSetChatBg(name, userId)
                }
            }
        }
    }


    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    val list = it.data as ArrayList<ChatBgBean>
                    mAdapter.setList(list)
                }

                is ChatBgBean -> {
                    dismiss()
                    EventBusUtils.postEvent(DecorationEvent(it))
                }
            }
        }
    }

    override fun initRequestData() {
        mViewModel.requestChatBgList(userId)
    }


}