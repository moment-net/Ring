package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.alan.module.my.R
import com.alan.module.my.adapter.LabelAdapter
import com.alan.module.my.databinding.LayoutDialogCardSelectBinding
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import com.alan.mvvm.base.utils.MyGridItemDecoration
import com.alan.mvvm.base.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardSelectFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogCardSelectBinding, EmptyViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()
    lateinit var mAdapter: LabelAdapter
    lateinit var tagName: String
    lateinit var tagType: String
    lateinit var checks: ArrayList<String>
    lateinit var values: ArrayList<String>
    var selectNum: Int = 1
    var listener: OnCheckListener? = null

    companion object {
        fun newInstance(
            tagName: String,
            tagType: String,
            checks: ArrayList<String>,
            values: ArrayList<String>
        ): CardSelectFragmentDialog {
            val args = Bundle()
            args.putString("tagName", tagName)
            args.putString("tagType", tagType)
            args.putStringArrayList("checks", checks)
            args.putStringArrayList("values", values)
            val fragment = CardSelectFragmentDialog()
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
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = dp2px(600f)
        params.gravity = Gravity.BOTTOM
        setCanceledOnTouchOutside(false)
        isCancelable = false
        window.attributes = params
    }

    override fun LayoutDialogCardSelectBinding.initView() {
        ivBack.clickDelay {
            dismiss()
            if (listener != null) {
                listener!!.onCheck(mAdapter.chooseList)
            }
        }

        arguments?.apply {
            tagName = getString("tagName", "")
            tagType = getString("tagType", "")
            checks = getStringArrayList("checks") as ArrayList<String>
            values = getStringArrayList("values") as ArrayList<String>
        }
        tvTitle.setText(tagName)
        if (TextUtils.equals(tagType, "option")) {
            tvNum.gone()
            selectNum = 1
        } else {
            tvNum.visible()
            selectNum = 5
        }

        initRV()
    }


    override fun initObserve() {

    }

    override fun initRequestData() {
    }

    fun initRV() {
        mAdapter = LabelAdapter()
        mBinding.rvList.apply {
            addItemDecoration(
                MyGridItemDecoration(
                    dp2px(20f),
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = mAdapter
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_title -> {
                    val title = mAdapter.data.get(position)

                    if (selectNum == 1) {
                        mAdapter.chooseList.clear()
                        mAdapter.chooseList.add(title)
                    } else {
                        if (mAdapter.chooseList.contains(title)) {
                            mAdapter.chooseList.remove(title)
                        } else {
                            if (mAdapter.chooseList.size == 5) {
                                toast("最多只能选择5个")
                                return@setOnItemChildClickListener
                            }
                            mAdapter.chooseList.add(title)
                        }
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
        mAdapter.chooseList = checks
        mAdapter.setList(values)
    }

    interface OnCheckListener {
        fun onCheck(list: List<String>)
    }
}