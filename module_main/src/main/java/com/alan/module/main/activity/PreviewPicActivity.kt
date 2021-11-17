package com.alan.module.main.activity

import android.Manifest
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.alan.module.main.R
import com.alan.module.main.adapter.PicVPAdapter
import com.alan.module.main.databinding.ActivityPreviewPicBinding
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.luck.picture.lib.PictureContentResolver
import com.luck.picture.lib.PictureMediaScannerConnection
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.thread.PictureThreadUtils
import com.luck.picture.lib.thread.PictureThreadUtils.SimpleTask
import com.luck.picture.lib.tools.*
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：图片预览
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_PREVIEW)
@AndroidEntryPoint
class PreviewPicActivity : BaseActivity<ActivityPreviewPicBinding, EmptyViewModel>() {
    val REQUESTED_PERMISSIONS = mutableListOf<String>(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()
    var picList: ArrayList<String> = arrayListOf()
    var mPosition: Int = 0
    var mType: Int = 0//0是本地；1是网络图片
    private var mMimeType: String? = null

    /**
     * 初始化View
     */
    override fun ActivityPreviewPicBinding.initView() {
        intent.apply {
            mType = getIntExtra("type", 0)
            mPosition = getIntExtra("position", 0)
            val list = getStringArrayListExtra("list")
            if (list != null && !list.isEmpty()) {
                picList.addAll(list)
            }
        }


        ivDownload.clickDelay {
            PermissionX.init(this@PreviewPicActivity).permissions(REQUESTED_PERMISSIONS[0])
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        showDialog("下载中...")
                        val downloadPath = picList.get(mPosition)
                        val currentMimeType = PictureMimeType.getImageMimeType(downloadPath)
                        mMimeType =
                            if (PictureMimeType.isJPG(currentMimeType)) PictureMimeType.MIME_TYPE_JPEG else currentMimeType
                        if (PictureMimeType.isHasHttp(downloadPath)) {
                            PictureThreadUtils.executeByIo(object : SimpleTask<String?>() {
                                override fun doInBackground(): String? {
                                    return showLoadingImage(downloadPath)
                                }


                                override fun onSuccess(filePath: String?) {
                                    dismissDialog()
                                    PictureThreadUtils.cancel(PictureThreadUtils.getIoPool())
                                    if (TextUtils.isEmpty(filePath)) {
                                        toast("图片保存失败")
                                    } else {
                                        PictureMediaScannerConnection(
                                            this@PreviewPicActivity,
                                            filePath,
                                            null
                                        )
                                        toast("图片保存成功至\n${filePath}")
                                    }
                                }
                            })
                        }
                    }
                }
        }

        if (mType == 0) {
            ivDownload.invisible()
        } else {
            ivDownload.visible()
        }

        tvNum.setText("${mPosition + 1}/${picList?.size}")

        val picVPAdapter = PicVPAdapter()
        viewpager.adapter = picVPAdapter
        picVPAdapter.setList(picList)
        viewpager.setCurrentItem(mPosition, false)
        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mPosition = position
                tvNum.setText("${position + 1}/${picList?.size}")
            }
        })
        picVPAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_pic -> {
                    finish()
                }
            }
        }
    }

    /**
     * 订阅数据
     */
    override fun initObserve() {

    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }


    /**
     * 下载图片至本地
     *
     * @param urlPath 图片网络url
     * @return
     */
    fun showLoadingImage(urlPath: String?): String? {
        var outImageUri: Uri? = null
        var outputStream: OutputStream? = null
        var inputStream: InputStream? = null
        try {
            outImageUri = if (SdkVersionUtils.checkedAndroid_Q()) {
                MediaUtils.createImageUri(this, "", mMimeType)
            } else {
                val suffix = PictureMimeType.getLastImgSuffix(mMimeType)
                val state = Environment.getExternalStorageState()
                val rootDir =
                    if (state == Environment.MEDIA_MOUNTED) Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                    ) else this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                if (!rootDir!!.exists()) {
                    rootDir!!.mkdirs()
                }
                val folderDir =
                    File(if (state != Environment.MEDIA_MOUNTED) rootDir.absolutePath else rootDir.absolutePath + File.separator + PictureMimeType.CAMERA + File.separator)
                if (!folderDir.exists()) {
                    folderDir.mkdirs()
                }
                val fileName = DateUtils.getCreateFileName("IMG_") + suffix
                val outFile = File(folderDir, fileName)
                Uri.fromFile(outFile)
            }
            outputStream =
                PictureContentResolver.getContentResolverOpenOutputStream(this, outImageUri)
            inputStream = URL(urlPath).openStream()
            val bufferCopy = PictureFileUtils.writeFileFromIS(inputStream, outputStream)
            if (bufferCopy) {
                return PictureFileUtils.getPath(this, outImageUri)
            }
        } catch (e: Exception) {
            if (SdkVersionUtils.checkedAndroid_Q()) {
                MediaUtils.deleteUri(this, outImageUri)
            }
        } finally {
            PictureFileUtils.close(inputStream)
            PictureFileUtils.close(outputStream)
        }
        return null
    }
}