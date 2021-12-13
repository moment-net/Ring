package com.alan.module.main.adapter

import android.graphics.Typeface
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.alan.module.main.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.ThinkBean
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.alan.mvvm.common.views.MultiImageView
import com.alan.mvvm.common.views.SmartGlideImageLoader
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.ImageViewerPopupView
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener
import leifu.shapelibrary.ShapeView


class ThinkAdapter(var activity: FragmentActivity) :
    BaseQuickAdapter<ThinkBean, BaseViewHolder>(R.layout.item_think),
    LoadMoreModule {

    init {
        addChildClickViewIds(R.id.iv_avatar)
        addChildClickViewIds(R.id.iv_more)
        addChildClickViewIds(R.id.iv_zan)
    }

    override fun convert(holder: BaseViewHolder, item: ThinkBean) {
        val ivAvatar = holder.getView<ImageView>(R.id.iv_avatar)
        CoilUtils.loadCircle(ivAvatar, item.user.avatar)
        holder.setText(R.id.tv_name, item.user.userName)
        val tvAge = holder.getView<ShapeView>(R.id.tv_age)
        val miv = holder.getView<MultiImageView>(R.id.miv)

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

        if (item.isFavorite) {
            holder.setTextColor(R.id.tv_num, R.color._FF6464.getResColor())
            holder.setImageResource(R.id.iv_zan, R.drawable.icon_home_zan_on)
        } else {
            holder.setTextColor(R.id.tv_num, R.color._BAB9B9.getResColor())
            holder.setImageResource(R.id.iv_zan, R.drawable.icon_home_zan_off)
        }
        holder.setText(R.id.tv_num, "${item.favoriteCount}")
        holder.getView<TextView>(R.id.tv_num).setTypeface(Typeface.MONOSPACE)
    }


}