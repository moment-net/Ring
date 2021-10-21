package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.alan.module.my.R
import com.alan.module.my.adapter.TagAdapter
import com.alan.module.my.databinding.LayoutDialogSkilBinding
import com.alan.module.my.viewmodol.TagViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.TargetBean
import com.alan.mvvm.base.http.responsebean.TargetInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.base.utils.MyGridItemDecoration
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.event.TagRefreshEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TagFragmentDialog : BaseFrameDialogFragment<LayoutDialogSkilBinding, TagViewModel>() {


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<TagViewModel>()
    private lateinit var mAdapter: TagAdapter
    private var allList: ArrayList<String> = arrayListOf()

    companion object {
        fun newInstance(): TagFragmentDialog {
            val args = Bundle()
            val fragment = TagFragmentDialog()
            fragment.arguments = args
            return fragment
        }
    }


    override fun LayoutDialogSkilBinding.initView() {
        ivClose.clickDelay { dismiss() }
        tvCommit.clickDelay {
            mViewModel.requestSaveTarget(mAdapter.selectList)
        }
        initRV()
    }

    private fun initRV() {
        mAdapter = TagAdapter()
        mBinding.rvTag.apply {
            addItemDecoration(
                MyGridItemDecoration(
                    dp2px(10f),
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.ctv_label -> {
                    val label = mAdapter.data.get(position)
                    if (mAdapter.selectList.contains(label)) {
                        mAdapter.selectList.remove(label)
                    } else {
                        if (mAdapter.selectList.size >= 3) {
                            toast("已选择三个，请取消再选择")
                        } else {
                            mAdapter.selectList.add(label)
                        }
                    }
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
        params.width = dp2px(300f)
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(true)
        isCancelable = true
        window.attributes = params
    }

    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is TargetBean -> {
                    allList.addAll(it.type!!)
                    mAdapter.setList(allList)
                }

                is BaseResponse<*> -> {
                    if (it.data != null) {
                        val targetInfoBean = it.data as TargetInfoBean
                        if (!mAdapter.selectList.isEmpty()) {
                            mAdapter.selectList.clear()
                        }
                        val tagList = targetInfoBean.typeTag
                        if (tagList != null && !tagList.isEmpty()) {
                            mAdapter.selectList.addAll(tagList)
                            mAdapter.notifyDataSetChanged()
                        }
                    }
                }

                is Boolean -> {
                    EventBusUtils.postEvent(TagRefreshEvent(1))
                    dismiss()
                }
            }
        }
    }

    override fun initRequestData() {
        mViewModel.requestList()
        mViewModel.requestTarget()
    }


}