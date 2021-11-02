package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.module.my.R
import com.alan.module.my.adapter.CardInfoAdapter
import com.alan.module.my.databinding.LayoutDialogCardinfoBinding
import com.alan.module.my.viewmodol.CardInfoViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.CardDetailBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.ClipboardUtil
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardInfoFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogCardinfoBinding, CardInfoViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<CardInfoViewModel>()
    lateinit var mAdapter: CardInfoAdapter
    lateinit var userId: String
    lateinit var name: String
    lateinit var cardBean: CardDetailBean


    companion object {
        fun newInstance(userId: String, name: String): CardInfoFragmentDialog {
            val args = Bundle()
            args.putString("userId", userId)
            args.putString("name", name)
            val fragment = CardInfoFragmentDialog()
            fragment.arguments = args
            return fragment
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
        setCanceledOnTouchOutside(false)
        isCancelable = false
        window.attributes = params
    }

    override fun LayoutDialogCardinfoBinding.initView() {
        arguments?.apply {
            userId = getString("userId", "")
            name = getString("name", "")
        }
        tvTitle.setText(name)

        ivClose.clickDelay { dismiss() }

        initRV()
    }


    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is CardDetailBean -> {
                    cardBean = it
                    CoilUtils.load(mBinding.image, cardBean.style.picUrl)
                    mAdapter.setList(it.tags)
                }
            }
        }
    }

    override fun initRequestData() {
        mViewModel.requestCardDetail(userId, name)
    }

    fun initRV() {
        mAdapter = CardInfoAdapter()
        mBinding.rvList.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0,
                    context.dp2px(10f),
                    ContextCompat.getColor(context, R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val bean = mAdapter.data.get(position)
            when (view.id) {
                R.id.tv_copy -> {
                    val value = bean.checkedValues.get(0).toString()
                    ClipboardUtil.copyText(requireActivity(), value)
                    toast("复制成功")
                }
            }
        }
    }


}