package com.alan.module.main.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alan.module.main.R
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import java.io.File


class GridImageAdapter(val context: Context) : RecyclerView.Adapter<GridImageAdapter.ViewHolder>() {
    private var mInflater: LayoutInflater
    var list: ArrayList<LocalMedia> = arrayListOf()
    private val selectMax = 9

    /**
     * 点击添加图片跳转
     */
    var mOnPicClickListener: OnPicClickListener? = null

    interface OnPicClickListener {
        fun onAddPicClick(position: Int)
        fun onDeletePicClick(position: Int)
        fun onPicClick(position: Int)
    }

    init {
        mInflater = LayoutInflater.from(context)
    }


    override fun getItemCount(): Int {
        return if (list.size < selectMax) {
            list.size + 1
        } else {
            list.size
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == list.size) {
            CoilUtils.load(holder.mImg, R.drawable.icon_pic_add)
            holder.mIvDel.gone()
            holder.mImg.setOnClickListener {
                mOnPicClickListener?.onAddPicClick(position)
            }
        } else {
            val item = list.get(position)

            val path = if (item.isCut() && !item.isCompressed()) {
                // 裁剪过没压缩
                item.getCutPath()
            } else if (item.isCut() || item.isCompressed()) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                item.getCompressPath()
            } else {
                // 原图
                item.getPath()
            }
            if (PictureMimeType.isContent(path) && !item.isCut() && !item.isCompressed()) {
                holder.mImg.load(Uri.parse(path)) {}
            } else {
                holder.mImg.load(File(path)) {}
            }
//            CoilUtils.load(holder.mImg,
//                if (PictureMimeType.isContent(path) && !item.isCut() && !item.isCompressed()) Uri.parse(
//                    path
//                ) else path)

            holder.mIvDel.visible()
            holder.mImg.setOnClickListener {
                mOnPicClickListener?.onPicClick(position)
            }
            holder.mIvDel.setOnClickListener {
                mOnPicClickListener?.onDeletePicClick(position)
            }
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mImg: ImageView
        var mIvDel: ImageView

        init {
            mImg = view.findViewById(R.id.iv_pic)
            mIvDel = view.findViewById(R.id.iv_del)
        }
    }


}