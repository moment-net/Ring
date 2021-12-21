package com.alan.module.my.activity

import android.Manifest
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import com.alan.module.my.databinding.ActivityPersonInfoBinding
import com.alan.module.my.dialog.LikesFragmentDialog
import com.alan.module.my.dialog.TagFragmentDialog
import com.alan.module.my.dialog.VoiceFragmentDialog
import com.alan.module.my.viewmodol.PersonInfoViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.baseresp.BaseResponse
import com.alan.mvvm.base.http.responsebean.FileBean
import com.alan.mvvm.base.http.responsebean.TargetInfoBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.utils.*
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.TagRefreshEvent
import com.alan.mvvm.common.event.UserEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.permissionx.guolindev.PermissionX
import com.socks.library.KLog
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.util.*


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：个人信息页面
 */
@EventBusRegister
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_PERSONINFO)
@AndroidEntryPoint
class PersonInfoActivity : BaseActivity<ActivityPersonInfoBinding, PersonInfoViewModel>() {
    val REQUESTED_PERMISSIONS = mutableListOf<String>(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO,
    )

    var birthday: String? = null
    var address: String? = null
    var imgUrl: String? = null

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<PersonInfoViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityPersonInfoBinding.initView() {
        ivBack.clickDelay { finish() }
        ivAvator.clickDelay {
//            ImageSelectUtil.singlePicCrop(this@PersonInfoActivity)
            jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_CHANGEAPPEARANCE)
        }

        tvBirthdayValue.clickDelay {
            changeBirthday()
        }

        tvHometownValue.clickDelay {
            changeAddress()
        }

        tvLikeValue.clickDelay {
            val dialog = LikesFragmentDialog.newInstance()
            dialog.show(supportFragmentManager)
        }

        tvLabelValue.clickDelay {
            val dialog = TagFragmentDialog.newInstance()
            dialog.show(supportFragmentManager)
        }

        tvPlay.clickDelay {
            MediaPlayUtil.play(SpHelper.getUserInfo()?.greeting?.audioPath)
        }


        tvVoiceValue.clickDelay {
            PermissionX.init(this@PersonInfoActivity).permissions(REQUESTED_PERMISSIONS)
                .request { allGranted, grantedList, deniedList ->
                    //不给权限可以进
                    if (allGranted) {
                        val voiceFragmentDialog = VoiceFragmentDialog.newInstance()
                        voiceFragmentDialog.show(this@PersonInfoActivity.supportFragmentManager)
                    }
                }
        }


        tvCommit.clickDelay { requestEditUserInfo() }




        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length!! > 0) {
                    tvCommit.setEnabled(true);
                } else {
                    tvCommit.setEnabled(false);
                }

                tvLimit.setText("${s?.length!!}/15");
            }
        })

        etDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                tvLimitDesc.setText("${s?.length!!}/15");
            }
        })

    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is FileBean -> {
                    //上传图片
                    imgUrl = it.fileName
                    CoilUtils.loadCircle(mBinding.ivAvator, it.fileUrl)
                }


                is UserInfoBean -> {
                    //用户信息更新
                    finish()
                }

                is BaseResponse<*> -> {
                    if (it.data != null) {
                        val targetInfoBean = it.data as TargetInfoBean
                        val tagList = targetInfoBean.typeTag
                        val likeList = targetInfoBean.likes
                        if (tagList == null || tagList.isEmpty()) {
                            mBinding.tvLabelValue.setText("请选择")
                        } else {
                            mBinding.tvLabelValue.setText("重新选择")
                        }

                        if (likeList == null || likeList.isEmpty()) {
                            mBinding.tvLikeValue.setText("请选择")
                        } else {
                            mBinding.tvLikeValue.setText("重新选择")
                        }
                    }
                }
            }
        }

    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }

    //更新信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updaeInfo(event: UserEvent) {
        setUserInfo()
    }

    //更新信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updaeTag(event: TagRefreshEvent) {
        mViewModel.requestTarget()
    }

    override fun onResume() {
        super.onResume()
        setUserInfo()
        mViewModel.requestTarget()
    }

    fun setUserInfo() {
        val userInfo = SpHelper.getUserInfo()
        CoilUtils.loadCircle(mBinding.ivAvator, userInfo?.avatar ?: "")
        if (userInfo?.gender == 1) {
            mBinding.rbBoy.isChecked = true
        } else {
            mBinding.rbGirl.isChecked = true
        }

        mBinding.etName.setText(userInfo?.userName)
        mBinding.etName.clearFocus()
        mBinding.etDesc.setText(userInfo?.desc)
        mBinding.etDesc.clearFocus()
        birthday = userInfo?.birthday
        mBinding.tvBirthdayValue.setText("${birthday}")
        address = userInfo?.address
        mBinding.tvHometownValue.setText("${address}")
//        if (userInfo?.greeting == null) {
//            mBinding.tvVoiceValue.setText("录制语音签名")
//            mBinding.tvPlay.gone()
//        } else {
//            mBinding.tvVoiceValue.setText("重新录制")
//            mBinding.tvPlay.visible()
//        }
    }


    /**
     * 更改地址
     */
    fun changeAddress() {
        LocationPickerUtil.showPickerView(
            this,
            object : LocationPickerUtil.OnPickerListener {
                override fun onPicker(opt1: String, opt2: String, opt3: String) {
                    address = opt1 + "-" + opt2 + "-" + opt3
                    mBinding.tvHometownValue.setText(address)
                }
            }
        )
    }

    /**
     * 生日
     */
    fun changeBirthday() {
        //时间选择器
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        startDate[1938, 3] = 8
        val timePickerBuilder = TimePickerBuilder(this) { date: Date?, v: View? ->
            birthday = getTime(date)
            mBinding.tvBirthdayValue.setText(birthday)
        }
            .setRangDate(startDate, endDate)
            .setDate(getData(2020, 5, 4))

        if (!TextUtils.isEmpty(SpHelper.getUserInfo()?.birthday)) {
            val birth: List<String> = SpHelper.getUserInfo()?.birthday!!.split("-")
            val birthday = IntArray(3)
            if (birth.size == 3) {
                try {
                    for (i in birth.indices) {
                        birthday[i] = birth[i].toInt()
                    }
                    timePickerBuilder.setDate(getData(birthday[0], birthday[1] - 1, birthday[2]))
                } catch (e: java.lang.Exception) {
                    e.message
                }
            }
        } else {
            timePickerBuilder.setDate(getData(1999, 5, 20))
        }
        val pvTime = timePickerBuilder.build()
        pvTime.show()
    }

    fun getTime(date: Date?): String? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.YEAR].toString() + "-" + (calendar[Calendar.MONTH] + 1) + "-" + calendar[Calendar.DATE]
    }

    fun getData(year: Int, month: Int, day: Int): Calendar? {
        val calendar = Calendar.getInstance()
        calendar[year, month] = day
        return calendar
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
                    if (selectList != null && !selectList.isEmpty()) {
                        val media = selectList.get(0)
                        val url = if (media.isCut) {
                            media.cutPath
                        } else {
                            media.getPath()
                        }
                        KLog.e("xujm", "原图地址::" + media.path)

                        if (media.isCut) {
                            KLog.e("xujm", "裁剪地址::" + media.cutPath)
                        }
                        if (media.isCompressed) {
                            KLog.e("xujm", "压缩地址::" + media.compressPath)
                            KLog.e(
                                "xujm",
                                "压缩后文件大小::" + File(media.compressPath).length() / 1024 + "k"
                            )
                        }
                        if (!TextUtils.isEmpty(media.androidQToPath)) {
                            KLog.e("xujm", "Android Q特有地址::" + media.androidQToPath)
                        }
                        if (media.isOriginal) {
                            KLog.e("xujm", "是否开启原图功能::" + true)
                            KLog.e("xujm", "开启原图功能后地址::" + media.originalPath)
                        }
                        KLog.e("xujm", "当前地址::" + url)
                        mViewModel.requestUploadPic(url)
                    }
                }
            }
        }
    }


    fun requestEditUserInfo() {
        mViewModel.requestEditUserInfo(
            mBinding.etName.text.toString(),
            mBinding.etDesc.text.toString(),
            birthday ?: "",
            address ?: ""
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayUtil.release()
    }
}