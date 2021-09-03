package com.alan.module.my.activity

import android.Manifest
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import com.alan.module.my.databinding.ActivityPersonInfoBinding
import com.alan.module.my.dialog.VoiceFragmentDialog
import com.alan.module.my.viewmodol.PersonInfoViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.FileBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.ktx.gone
import com.alan.mvvm.base.ktx.visible
import com.alan.mvvm.base.utils.*
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.event.UserEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@EventBusRegister
@Route(path = RouteUrl.MyModule.ACTIVITY_MY_PERSONINFO)
@AndroidEntryPoint
class PersonInfoActivity : BaseActivity<ActivityPersonInfoBinding, PersonInfoViewModel>() {

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
            requestPermission()
        }

        tvBirthdayValue.clickDelay {
            changeBirthday()
        }

        tvHometownValue.clickDelay {
            changeAddress()
        }

        tvPlay.clickDelay {
            MediaPlayUtil.play(SpHelper.getUserInfo()?.greeting?.audioPath)
        }


        tvVoiceValue.clickDelay {
            var voiceFragmentDialog = VoiceFragmentDialog.newInstance()
            voiceFragmentDialog.show(this@PersonInfoActivity.supportFragmentManager)
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

    override fun onResume() {
        super.onResume()
        setUserInfo()
    }

    fun setUserInfo() {
        var userInfo = SpHelper.getUserInfo()
        CoilUtils.loadCircle(mBinding.ivAvator, userInfo?.avatar ?: "")
        if (userInfo?.gender == 1) {
            mBinding.rbBoy.isChecked = true
        } else {
            mBinding.rbGirl.isChecked = true
        }

        mBinding.etName.setText(userInfo?.userName)
        mBinding.etName.clearFocus()
        mBinding.tvBirthdayValue.setText("${userInfo?.birthday}")
        mBinding.tvHometownValue.setText("${userInfo?.address}")
        if (userInfo?.greeting == null) {
            mBinding.tvVoiceValue.setText("录制语音签名")
            mBinding.tvPlay.gone()
        } else {
            mBinding.tvVoiceValue.setText("重新录制")
            mBinding.tvPlay.visible()
        }
    }


    fun requestPermission() {
        PermissionX.init(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request { allGranted, grantedList, deniedList ->
                //不给权限可以进
                if (allGranted) {
                    this@PersonInfoActivity?.let { ImageSelectUtil.singlePic(it) }
                } else {
                    toast("没有图片读取权限")
                }
            }
    }

    fun changeAddress() {
        LocationPickerUtil.showPickerView(
            this,
            object : LocationPickerUtil.OnPickerListener {
                override fun onPicker(opt1: String, opt2: String, opt3: String) {
                    address = opt1.toString() + "-" + opt2 + "-" + opt3
                    mBinding.tvHometownValue.setText(opt3)
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
                ImageSelectUtil.REQUESTCODE -> {
                    //照片的回调
                    val selectList =
                        data?.getParcelableArrayListExtra<Photo>(EasyPhotos.RESULT_PHOTOS)
                    val url: String = selectList?.get(0)?.path ?: ""
                    mViewModel.requestUploadPic(url)
                }
            }
        }
    }


    fun requestEditUserInfo() {
        mViewModel.requestEditUserInfo(
            mBinding.etName.text.toString(),
            imgUrl ?: "",
            birthday ?: "",
            address ?: ""
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayUtil.release()
    }
}