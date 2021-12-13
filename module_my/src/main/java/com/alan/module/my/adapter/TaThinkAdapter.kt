package com.alan.module.my.adapter

import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.alan.module.my.R
import com.alan.mvvm.base.http.responsebean.ThinkBean
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


class TaThinkAdapter(var activity: FragmentActivity) :
    BaseQuickAdapter<ThinkBean, BaseViewHolder>(R.layout.item_think_ta),
    LoadMoreModule {

    init {
        addChildClickViewIds(R.id.iv_zan)
    }

    override fun convert(holder: BaseViewHolder, item: ThinkBean) {
        holder.setText(R.id.tv_content, item.content)
        holder.setText(R.id.tv_time, item.createTime)
        val miv = holder.getView<MultiImageView>(R.id.miv)

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
    }


}