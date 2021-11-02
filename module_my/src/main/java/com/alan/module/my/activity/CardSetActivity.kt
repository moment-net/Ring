package com.alan.module.my.activity

import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alan.module.my.R
import com.alan.module.my.adapter.CardItemAdapter
import com.alan.module.my.databinding.ActivityCardSetBinding
import com.alan.module.my.dialog.CardInputFragmentDialog
import com.alan.module.my.dialog.CardSelectFragmentDialog
import com.alan.module.my.viewmodol.CardSetViewModel
import com.alan.mvvm.base.http.requestbean.Tag
import com.alan.mvvm.base.http.responsebean.CardDetailBean
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.MyColorDecoration
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_CARDSET)
@AndroidEntryPoint
class CardSetActivity : BaseActivity<ActivityCardSetBinding, CardSetViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<CardSetViewModel>()
    lateinit var mAdapter: CardItemAdapter
    lateinit var cardBean: CardDetailBean

    @JvmField
    @Autowired
    var userId = ""

    @JvmField
    @Autowired
    var name = ""

    fun initWindow() {
        val params = window!!.attributes
        val colorDrawable = ColorDrawable(R.color.transparent.getResColor())
        window.setBackgroundDrawable(colorDrawable)
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = dp2px(600f)
        params.gravity = Gravity.BOTTOM
        window.attributes = params
    }

    /**
     * 初始化View
     */
    override fun ActivityCardSetBinding.initView() {
        initWindow()

        ivClose.clickDelay { finish() }
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
            if (cardBean.exist) {
                mViewModel.requestEditCard(cardBean.id, cardBean.cardName, tags)
            } else {
                mViewModel.requestAddCard(cardBean.cardName, tags)
            }
        }
        tvDelete.clickDelay {
            mViewModel.requestDeleteCard(cardBean.id)
        }


        tvTitle.setText(name)

        initRV()
    }

    /**
     * 订阅数据
     */
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
                    finish()
                }
                2 -> {
                    //编辑成功
                    finish()
                }
                3 -> {
                    //删除成功
                    finish()
                }
            }
        }
    }

    /**
     * 获取数据
     */
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
                    ContextCompat.getColor(this@CardSetActivity, R.color.white)
                )
            )
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val bean = mAdapter.data.get(position)
            val tagName = bean.tagName
            if (TextUtils.equals(bean.tagType, "input")) {
                val name = if (bean.checkedValues == null) {
                    ""
                } else {
                    bean.checkedValues.get(0)
                }
                val dialog = CardInputFragmentDialog.newInstance(tagName, name)
                dialog.show(supportFragmentManager)
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
                dialog.show(supportFragmentManager)
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