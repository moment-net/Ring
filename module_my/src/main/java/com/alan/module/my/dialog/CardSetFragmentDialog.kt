package com.alan.module.my.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.module.my.R
import com.alan.module.my.adapter.CardItemAdapter
import com.alan.module.my.databinding.LayoutDialogCardBinding
import com.alan.module.my.viewmodol.CardSetViewModel
import com.alan.mvvm.base.http.requestbean.Tag
import com.alan.mvvm.base.http.responsebean.CardDetailBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.alan.mvvm.base.mvvm.v.BaseFrameDialogFragment
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardSetFragmentDialog :
    BaseFrameDialogFragment<LayoutDialogCardBinding, CardSetViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<CardSetViewModel>()
    lateinit var mAdapter: CardItemAdapter
    lateinit var userId: String
    lateinit var name: String
    var cardBean: CardDetailBean? = null


    companion object {
        fun newInstance(userId: String, name: String): CardSetFragmentDialog {
            val args = Bundle()
            args.putString("userId", userId)
            args.putString("name", name)
            val fragment = CardSetFragmentDialog()
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

    override fun LayoutDialogCardBinding.initView() {
        ivClose.clickDelay { dismiss() }
        tvCommit.clickDelay {
            val tags = arrayListOf<Tag>()
            for (item in mAdapter.data) {
                if (item.checkedValues == null || item.checkedValues.isEmpty()) {
                    val tip = if (TextUtils.equals(item.tagType, "input")) {
                        "请输入"
                    } else {
                        "请选择"
                    }
                    toast("$tip${item.tagName}")
                    return@clickDelay
                } else {
                    val tag = Tag(item.tagName, item.checkedValues)
                    tags.add(tag)
                }
            }
            if (cardBean == null) {
                return@clickDelay
            }
            if (cardBean?.exist!!) {
                mViewModel.requestEditCard(cardBean?.id!!, cardBean?.cardName!!, tags)
            } else {
                mViewModel.requestAddCard(cardBean?.cardName!!, tags)
            }
        }
        tvDelete.clickDelay {
            if (cardBean == null) {
                return@clickDelay
            }
            mViewModel.requestDeleteCard(cardBean?.id!!)
        }

        arguments?.apply {
            userId = getString("userId", "")
            name = getString("name", "")
        }
        tvTitle.setText(name)

        initRV()
    }


    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is CardDetailBean -> {
                    cardBean = it
                    if (it.exist) {
                        mBinding.tvDelete.visible()
                    } else {
                        mBinding.tvDelete.gone()
                    }
                    mAdapter.setList(it.tags)

                }
                1 -> {
                    //增加成功
                    dismiss()
                }
                2 -> {
                    //编辑成功
                    dismiss()
                }
                3 -> {
                    //删除成功
                    dismiss()
                }
            }
        }
    }

    override fun initRequestData() {
        mViewModel.requestCardDetail(userId, name)
    }

    fun initRV() {
        mAdapter = CardItemAdapter()
        mBinding.rvList.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, 0,
                    dp2px(20f),
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = mAdapter.data.get(position)
            val tagName = bean.tagName
            val limit = bean.limit
            if (TextUtils.equals(bean.tagType, "input")) {
                val name = if (bean.checkedValues == null) {
                    ""
                } else {
                    bean.checkedValues.get(0)
                }
                val dialog = CardInputFragmentDialog.newInstance(tagName, name, limit)
                dialog.show(requireActivity().supportFragmentManager)
                dialog.inputListener = object : CardInputFragmentDialog.OnInputListener {
                    override fun onInput(input: String) {
                        val arrayListOf = arrayListOf<String>()
                        arrayListOf.add(input)
                        mAdapter.data.get(position).checkedValues = arrayListOf
                        mAdapter.notifyItemChanged(position)
                    }
                }
            } else {
                val dialog = CardSelectFragmentDialog.newInstance(
                    tagName,
                    bean.tagType,
                    bean.checkedValues ?: arrayListOf(),
                    bean.values
                )
                dialog.show(requireActivity().supportFragmentManager)
                dialog.listener = object : CardSelectFragmentDialog.OnCheckListener {
                    override fun onCheck(list: List<String>) {
                        val arrayListOf = arrayListOf<String>()
                        arrayListOf.addAll(list)
                        mAdapter.data.get(position).checkedValues = arrayListOf
                        mAdapter.notifyItemChanged(position)
                    }
                }
            }
        }
    }


}