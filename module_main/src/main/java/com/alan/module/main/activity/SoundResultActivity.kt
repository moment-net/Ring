package com.alan.module.main.activity

import android.media.MediaPlayer
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.alan.module.main.R
import com.alan.module.main.databinding.ActivitySoundResultBinding
import com.alan.module.main.dialog.ShareFragmentDialog
import com.alan.module.main.viewmodel.SoundResultViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.responsebean.SoundResultBean
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.im.utils.VoicePlayerUtil
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.MainModule.ACTIVITY_MAIN_SOUNDRESULT)
@AndroidEntryPoint
class SoundResultActivity : BaseActivity<ActivitySoundResultBinding, SoundResultViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<SoundResultViewModel>()

    @JvmField
    @Autowired(name = "bean")
    var bean: SoundResultBean? = null

    /**
     * 初始化View
     */
    override fun ActivitySoundResultBinding.initView() {
        ivBack.clickDelay { finish() }

        ivVoice.clickDelay {
            VoicePlayerUtil.getInstance(this@SoundResultActivity)
                .play(bean?.fileUrl, MediaPlayer.OnCompletionListener {
                })
        }

        ivShare.clickDelay {
            val dialog = ShareFragmentDialog.newInstance(bean!!)
            dialog.show(supportFragmentManager)
        }

    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        if (bean != null) {
            val userInfo = SpHelper?.getUserInfo()
            CoilUtils.loadRoundBorder(
                mBinding.ivAvatar,
                userInfo?.avatar!!,
                32f,
                1f,
                ContextCompat.getColor(this, R.color.white)
            )
            mBinding.tvName.setText(userInfo?.userName)
            mBinding.tvStar.setText("${bean?.highlySimilar}")
            mBinding.tvAttr.setText("声音属性是：${bean?.attribute}")
            val similarity = bean?.similarity
            val doubleList = arrayListOf<Double>()
            val stringList = arrayListOf<String>()
            if (similarity != null) {
                for (bean in similarity) {
                    val sims = bean.sims
                    val userName = bean.userName
                    doubleList.add(sims)
                    stringList.add(userName)
                }
            }
            mBinding.spiderview.setRankData(
                doubleList.toDoubleArray(),
                stringList.toTypedArray()
            )
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {
    }
}