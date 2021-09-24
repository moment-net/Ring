package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.alan.module.my.R
import com.alan.module.my.adapter.LikeAdapter
import com.alan.module.my.databinding.LayoutDialogLikeBinding
import com.alan.module.my.viewmodol.LikeViewModel
import com.alan.mvvm.base.http.responsebean.TargetBean
import com.alan.mvvm.base.http.responsebean.TargetInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.MyGridItemDecoration
import com.alan.mvvm.base.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikesFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogLikeBinding, LikeViewModel>() {


    private var allList: ArrayList<String> = arrayListOf()

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<LikeViewModel>()
    private lateinit var mAdapter: LikeAdapter

    companion object {
        fun newInstance(): LikesFragmentDialog {
            val args = Bundle()
            val fragment = LikesFragmentDialog()
            fragment.arguments = args
            return fragment
        }
    }


    override fun LayoutDialogLikeBinding.initView() {
        ivClose.clickDelay { dismiss() }
        tvCommit.clickDelay {
            mViewModel.requestSaveTarget(mAdapter.selectList)
        }
        initRV()
    }

    private fun initRV() {
        mAdapter = LikeAdapter()
        mBinding.rvLike.apply {
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
                    allList.addAll(it.like!!)
                    mAdapter.setList(allList)
                }

                is TargetInfoBean -> {
                    if (!mAdapter.selectList.isEmpty()) {
                        mAdapter.selectList.clear()
                    }
                    mAdapter.selectList.addAll(it.likes)
                    mAdapter.notifyDataSetChanged()
                }

                is Boolean -> {
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