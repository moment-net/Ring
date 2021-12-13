package com.alan.module.main.adapter

import android.animation.ValueAnimator
import android.graphics.Color
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.alan.module.main.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.NowBean
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.views.MultiImageView
import com.alan.mvvm.common.views.SmartGlideImageLoader
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.ImageViewerPopupView
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener
import leifu.shapelibrary.ShapeView


class NowAdapter(var activity: FragmentActivity) :
    BaseQuickAdapter<NowBean, BaseViewHolder>(R.layout.item_now), LoadMoreModule {
    var listener: OnReplyClickListener? = null


    init {
        addChildClickViewIds(R.id.iv_avatar)
        addChildClickViewIds(R.id.iv_more)
    }

    override fun convert(holder: BaseViewHolder, item: NowBean) {
        val ivAvatar = holder.getView<ImageView>(R.id.iv_avatar)
        val tvAge = holder.getView<ShapeView>(R.id.tv_age)
        val tvLabelBg = holder.getView<ShapeView>(R.id.tv_label_bg)
        val ivLabel = holder.getView<ImageView>(R.id.iv_label)
        val tvLabel = holder.getView<TextView>(R.id.tv_label)
        val miv = holder.getView<MultiImageView>(R.id.miv)
        val clInput = holder.getView<ConstraintLayout>(R.id.cl_input)
        val ivAvatarMy = holder.getView<ImageView>(R.id.iv_avatar_my)
        val etContent = holder.getView<ShapeView>(R.id.et_content)
        val ivChat = holder.getView<ImageView>(R.id.iv_chat)

        val itemPosition = getItemPosition(item)

        CoilUtils.loadCircle(ivAvatar, item.user.avatar)
        holder.setText(R.id.tv_name, item.user.userName)
        CoilUtils.loadCircle(ivAvatarMy, SpHelper.getUserInfo()?.avatar!!)


        val age = if (item.user.age > 0) {
            "${item.user.age}岁"
        } else {
            ""
        }
        if (TextUtils.isEmpty(age)) {
            tvAge.setText("")
            tvAge.compoundDrawablePadding = 0
        } else {
            tvAge.setText("$age")
            tvAge.compoundDrawablePadding = context.dp2px(2f)
        }
        if (item.user.gender == 1) {
            tvAge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_home_boy_blue, 0, 0, 0)
            tvAge.setTextColor(R.color._515FFF.getResColor())
            tvAge.setShapeSolidColor(R.color._33515FFF.getResColor()).setUseShape()
        } else {
            tvAge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_home_girl_blue, 0, 0, 0)
            tvAge.setTextColor(R.color._FF516D.getResColor())
            tvAge.setShapeSolidColor(R.color._33FF516D.getResColor()).setUseShape()
        }

        if (item.user.onlineStatus!!) {
            holder.setGone(R.id.tv_online, false)
        } else {
            holder.setGone(R.id.tv_online, true)
        }

        holder.setText(R.id.tv_time, item.createTimeDesc)
        holder.setText(R.id.tv_content, item.content)

        val list = item.pic
        if (list != null && list.size > 0) {
            miv.visible()
            miv.setList(list)

            miv.setOnItemClickListener { view, position ->
                val picList = arrayListOf<String>()
                for (bean in list) {
                    picList.add(bean.url)
                }

                XPopup.Builder(activity).asImageViewer(
                    view as ImageView?, position,
                    picList as List<String>?, object : OnSrcViewUpdateListener {
                        override fun onSrcViewUpdate(
                            popupView: ImageViewerPopupView,
                            position: Int
                        ) {
                            popupView.updateSrcView(miv.imageViewList.get(position))
                        }
                    }, SmartGlideImageLoader()
                ).show()


//                val bundle = Bundle().apply {
//                    putStringArrayList("list", picList)
//                    putInt("position", position)
//                    putInt("type", 1)
//                }
//                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_PREVIEW, bundle)
            }
        } else {
            miv.gone()
        }

        //设置标签
        tvLabelBg.setShapeSolidColor(Color.parseColor(item.bgColor)).setUseShape()
        tvLabelBg.alpha = item.bgOpacity
        tvLabel.setText(item.tag)
        tvLabel.setTextColor(Color.parseColor(item.textColor))
        CoilUtils.load(ivLabel, item.tagPicUrl)

        if (item.isShow) {
            ivAvatarMy.visible()
            etContent.visible()
            val layoutParams = clInput.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topMargin = context.dp2px(56f)
            clInput.layoutParams = layoutParams
        } else {
            ivAvatarMy.gone()
            etContent.gone()
            val layoutParams = clInput.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topMargin = context.dp2px(10f)
            clInput.layoutParams = layoutParams
        }

        ivChat.setOnClickListener {
            if (!item.isShow) {
                item.isShow = true
                ivAvatarMy.visible()
                etContent.visible()
                startAnimal(clInput)
            } else {
                if (listener != null && !TextUtils.isEmpty(etContent.text.toString())) {
                    etContent.requestFocus()
                    listener!!.onReply(itemPosition, etContent.text.toString())
                    etContent.setText("")
                }
            }
        }
    }

    fun startAnimal(cl: ConstraintLayout) {
        val animator = ValueAnimator.ofInt(context.dp2px(10f), context.dp2px(56f))
        animator.addUpdateListener {
            val animatedValue = it.animatedValue as Int
            val layoutParams = cl.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topMargin = animatedValue
            cl.layoutParams = layoutParams
        }
        animator.setDuration(500)
        animator.start()
    }

    interface OnReplyClickListener {
        fun onReply(position: Int, content: String)
    }
}