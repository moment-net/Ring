package com.alan.module.home.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.alan.module.main.R
import com.alan.module.main.adapter.StarAdapter
import com.alan.module.main.databinding.LayoutStarBinding
import com.alan.module.main.viewmodel.RemindViewModel
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.MyGridItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StarFragmentDialog : BaseFrameDialogFragment<LayoutStarBinding, RemindViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<RemindViewModel>()

    private lateinit var mAdapter: StarAdapter


    companion object {
        fun newInstance(userId: String): StarFragmentDialog {
            val bundle = Bundle()
            bundle.putString("userId", userId)
            val dialog = StarFragmentDialog()
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


    override fun LayoutStarBinding.initView() {
        initRV()
    }

    private fun initRV() {
        mAdapter = StarAdapter()
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
//                    val list = mAdapter.data
//                    for (bean in list){
//                        bean.selected = false
//                    }
//                    list.get(position).selected = true
//                    mAdapter.notifyDataSetChanged()
//
//                    val name = list.get(position).name
//                    mViewModel.requestSetChatBg(name,userId)
                }
            }
        }
    }


    override fun initObserve() {
//        mViewModel.ldSuccess.observe(this){
//            when(it){
//                is BaseResponse<*> ->{
//                    val list = it.data as ArrayList<ChatBgBean>
//                    mAdapter.setList(list)
//                }
//
//                is ChatBgBean ->{
//                    dismiss()
//                    EventBusUtils.postEvent(DecorationEvent(it))
//                }
//            }
//        }
    }

    override fun initRequestData() {
//        mViewModel.requestChatBgList(userId)
    }


}