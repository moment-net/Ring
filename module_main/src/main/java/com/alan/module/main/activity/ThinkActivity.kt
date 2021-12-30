package com.alan.module.main.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.alan.module.main.R
import com.alan.module.main.adapter.GridImageAdapter
import com.alan.module.main.databinding.ActivityThinkBinding
import com.alan.module.main.viewmodel.PushThinkViewModel
import com.alan.mvvm.base.http.responsebean.PicBean
import com.alan.mvvm.base.http.responsebean.StsTokenBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.ktx.getResDrawable
import com.alan.mvvm.base.utils.*
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.dialog.DialogHelper
import com.alan.mvvm.common.event.ChangeThinkEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import com.socks.library.KLog
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：发布想法
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_THINK)
@AndroidEntryPoint
class ThinkActivity : BaseActivity<ActivityThinkBinding, PushThinkViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<PushThinkViewModel>()
    var content: String = ""
    lateinit var picAdapter: GridImageAdapter
    var picMap = hashMapOf<Int, PicBean>()
    var picList: ArrayList<PicBean> = arrayListOf()
    private val selectMax = 9

    /**
     * 初始化View
     */
    override fun ActivityThinkBinding.initView() {
        ivBack.clickDelay { finish() }
        tvRight.clickDelay {
            content = etContent.text.toString()
            if (TextUtils.isEmpty(content)) {
                toast("请输入您的想法")
                return@clickDelay
            }
            mBinding.etContent.clearFocus()
            if (picAdapter.list.isEmpty()) {
                mViewModel.requestPushThink(content, picList)
            } else {
                mViewModel.requestToken()
            }

        }

        etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length!! > 0) {
                    changeBt(true)
                } else {
                    changeBt(false)
                }

                tvLimit.setText("${s?.length!!}/500");
            }
        })
        mBinding.etContent.isFocusable = true
        mBinding.etContent.isFocusableInTouchMode = true
        mBinding.etContent.requestFocus()
        initRvPic()
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is Boolean -> {
                    toast("发布成功")
                    EventBusUtils.postEvent(ChangeThinkEvent(1))
                    finish()
                }

                is StsTokenBean -> {
                    requestUploadPic(it)
                }
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }

    override fun onResume() {
        super.onResume()
        showDialog()
    }

    fun changeBt(enable: Boolean) {
        if (enable) {
            mBinding.tvRight.isEnabled = true
            mBinding.tvRight.setTextColor(R.color.white.getResColor())
            mBinding.tvRight.background = R.drawable.icon_push_bt_enable.getResDrawable()
        } else {
            mBinding.tvRight.isEnabled = false
            mBinding.tvRight.setTextColor(R.color._80393939.getResColor())
            mBinding.tvRight.background = R.drawable.icon_push_bt_disenable.getResDrawable()
        }
    }


    fun showDialog() {
        //是否有通知权限
        val userInfo = SpHelper.getUserInfo()
        if (!userInfo?.setVoice!!) {
            DialogHelper.showMultipleDialog(this, "先去测测你的声音吧", "测一测", "我再想想", {
                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_SOUND)
            }, {
                finish()
            })
        }
    }

    fun initRvPic() {
        picAdapter = GridImageAdapter(this)
        mBinding.rvPic.apply {
            addItemDecoration(
                MyGridItemDecoration(
                    dp2px(2f),
                    ContextCompat.getColor(context, R.color.white)
                )
            )
            layoutManager =
                GridLayoutManager(this@ThinkActivity, 3, GridLayoutManager.VERTICAL, false)
            adapter = picAdapter
        }

        picAdapter.mOnPicClickListener = object : GridImageAdapter.OnPicClickListener {
            override fun onAddPicClick(position: Int) {
                //点击添加图片
                ImageSelectUtil.multiplePic(this@ThinkActivity, picAdapter.list)
            }

            override fun onDeletePicClick(position: Int) {
                picAdapter.list.removeAt(position)
                picAdapter.notifyItemRemoved(position)
            }

            override fun onPicClick(position: Int) {
                val picList = arrayListOf<String>()
                for (bean in picAdapter.list) {
                    picList.add(bean.path)
                }
                val bundle = Bundle().apply {
                    putStringArrayList("list", picList)
                    putInt("position", position)
                    putInt("type", 0)
                }
                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_PREVIEW, bundle)
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 例如 LocalMedia 里面返回五种path
                    // 1.media.getPath(); 原图path
                    // 2.media.getCutPath();裁剪后path，需判断media.isCut();切勿直接使用
                    // 3.media.getCompressPath();压缩后path，需判断media.isCompressed();切勿直接使用
                    // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                    // 5.media.getAndroidQToPath();Android Q版本特有返回的字段，但如果开启了压缩或裁剪还是取裁剪或压缩路径；注意：.isAndroidQTransform 为false 此字段将返回空
                    // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
                    val selectList = PictureSelector.obtainMultipleResult(data)

                    if (!picAdapter.list.isEmpty()) {
                        picAdapter.list.clear()
                    }
                    picAdapter.list.addAll(selectList)
                    picAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun requestUploadPic(bean: StsTokenBean) {
        showDialog("上传中...")
        if (!picMap.isEmpty()) {
            picMap.clear()
        }
        if (!picList.isEmpty()) {
            picList.clear()
        }
        val listener = object : OssManager.OnUploadListener {
            override fun onProgress(position: Int, currentSize: Long, totalSize: Long) {
//                KLog.e("uploadPic", "上传的第几个:${position}===上传进度${totalSize}===当前进度$currentSize")
            }

            override fun onSuccess(position: Int, item: LocalMedia, imageUrl: String?) {
                KLog.e(
                    "uploadPic",
                    "当前线程${Thread.currentThread().name}==========上传成功${item.compressPath}"
                )
                runOnUiThread {
                    picMap.put(position, PicBean(imageUrl!!, item.width, item.height))
                    KLog.e(
                        "uploadPic",
                        "上传成功${item.compressPath} ==${picMap.size}==${picAdapter.list.size}== ${imageUrl}"
                    )
                    if (picMap.size == picAdapter.list.size) {
                        for (i in 0..picMap.size - 1) {
                            picList.add(picMap.get(i)!!)
                        }
                        mViewModel.requestPushThink(content, picList)
                    }
                }
            }

            override fun onFailure(position: Int) {
                KLog.e("uploadPic", "上传失败")
            }
        }

        for (i in 0 until picAdapter.list.size) {
            val item = picAdapter.list.get(i)
            OssManager.mInstance.upload(this, bean, i, item, listener)
        }
    }

}