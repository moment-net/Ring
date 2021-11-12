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
import com.alan.module.main.adapter.NowLabelAdapter
import com.alan.module.main.databinding.ActivityNowBinding
import com.alan.module.main.dialog.CreateTagFragmentDialog
import com.alan.module.main.viewmodel.PushNowViewModel
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.NowTagBean
import com.alan.mvvm.base.http.responsebean.PicBean
import com.alan.mvvm.base.http.responsebean.StsTokenBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.dp2px
import com.alan.mvvm.base.ktx.getResColor
import com.alan.mvvm.base.ktx.getResDrawable
import com.alan.mvvm.base.utils.*
import com.alan.mvvm.base.utils.OssManager.OnUploadListener
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.ChangeThinkEvent
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import com.socks.library.KLog
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：发布现在
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_NOW)
@AndroidEntryPoint
class NowActivity : BaseActivity<ActivityNowBinding, PushNowViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<PushNowViewModel>()
    lateinit var mAdapter: NowLabelAdapter
    lateinit var picAdapter: GridImageAdapter
    var picList: ArrayList<PicBean> = arrayListOf()
    var tag: String = ""
    var content: String = ""
    private val selectMax = 9

    /**
     * 初始化View
     */
    override fun ActivityNowBinding.initView() {
        ivBack.clickDelay { finish() }
        tvRight.clickDelay {
            content = etContent.text.toString()
            if (TextUtils.isEmpty(content)) {
                toast("请输入您的想法")
                return@clickDelay
            }
            if (TextUtils.isEmpty(tag)) {
                tag = "正在"
            }
            mBinding.etContent.clearFocus()
            if (picAdapter.list.isEmpty()) {
                mViewModel.requestPushNow(tag, content, picList)
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

        initRv()
        initRvPic()
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldData.observe(this) {
            when (it) {
                is BaseResponse<*> -> {
                    val list = it.data as ArrayList<NowTagBean>

                    mAdapter.setList(list)
                }

                is Boolean -> {
                    toast("发布成功")
                    EventBusUtils.postEvent(ChangeThinkEvent(0))
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
        mViewModel.requestNowTagList()
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

    fun initRv() {
        mAdapter = NowLabelAdapter()
        mBinding.rvList.apply {
            addItemDecoration(
                MyColorDecoration(
                    0, 0, dp2px(10f), dp2px(10f),
                    ContextCompat.getColor(context, R.color.white)
                )
            )
            layoutManager = FlexboxLayoutManager(this@NowActivity, FlexDirection.ROW, FlexWrap.WRAP)
            adapter = mAdapter
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_label_bg -> {
                    if (position == mAdapter.data.size - 1) {
                        val inputDialog = CreateTagFragmentDialog.newInstance()
                        inputDialog.show(supportFragmentManager)
                        inputDialog.listener = object : CreateTagFragmentDialog.OnCompleteListener {
                            override fun onComplete() {
                                mViewModel.requestNowTagList()
                            }
                        }
                    } else {
                        mAdapter.selectPosition = position
                        mAdapter.notifyDataSetChanged()
                        tag = mAdapter.data.get(position).tag
                        mBinding.etContent.setText(mAdapter.data.get(position).defaultText)
                        mBinding.etContent.setSelection(mBinding.etContent.text.length)
                    }
                }
            }
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
                GridLayoutManager(this@NowActivity, 3, GridLayoutManager.VERTICAL, false)
            adapter = picAdapter
        }

        picAdapter.mOnPicClickListener = object : GridImageAdapter.OnPicClickListener {
            override fun onAddPicClick(position: Int) {
                //点击添加图片
                ImageSelectUtil.multiplePic(this@NowActivity, picAdapter.list)
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
        showDialog()

        val listener = object : OnUploadListener {
            override fun onProgress(position: Int, currentSize: Long, totalSize: Long) {
                KLog.e("uploadPic", "上传进度${totalSize} ===$currentSize")
            }

            override fun onSuccess(position: Int, item: LocalMedia, imageUrl: String?) {
                picList.add(position, PicBean(imageUrl!!, item.width, item.height))
                KLog.e(
                    "uploadPic",
                    "上传成功${item.realPath} ==${picList.size}==${picAdapter.list.size}== ${imageUrl}"
                )
                if (picList.size == picAdapter.list.size) {
                    mViewModel.requestPushNow(tag, content, picList)
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