package com.alan.module.chat.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import cn.jpush.android.api.JPushInterface
import com.alan.module.chat.R
import com.alan.module.chat.adapter.ViewpagerAdapter
import com.alan.module.chat.databinding.ActivityChatBinding
import com.alan.module.chat.fragment.ChatDetailFragment
import com.alan.module.chat.fragment.SpeakFragment
import com.alan.module.chat.view.VoiceRecorderView
import com.alan.module.chat.viewmodol.ChatDetailViewModel
import com.alan.mvvm.base.coil.CoilUtils
import com.alan.mvvm.base.http.apiservice.HttpBaseUrlConstant
import com.alan.mvvm.base.http.responsebean.CallBean
import com.alan.mvvm.base.http.responsebean.MatchStatusBean
import com.alan.mvvm.base.http.responsebean.SpeakVoiceBean
import com.alan.mvvm.base.http.responsebean.UserInfoBean
import com.alan.mvvm.base.ktx.*
import com.alan.mvvm.base.utils.*
import com.alan.mvvm.common.constant.Constants
import com.alan.mvvm.common.constant.IMConstant
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.db.entity.UserEntity
import com.alan.mvvm.common.dialog.DialogHelper
import com.alan.mvvm.common.event.EMMsgEvent
import com.alan.mvvm.common.helper.SpHelper
import com.alan.mvvm.common.im.EMClientHelper
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.hyphenate.chat.*
import com.hyphenate.chat.EMMessage
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.ChatModule.ACTIVITY_CHAT_DETAIL)
@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding, ChatDetailViewModel>() {
    val REQUESTED_PERMISSIONS = mutableListOf<String>(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA
    )

    @JvmField
    @Autowired
    var userId = ""

    @JvmField
    @Autowired
    var content = ""

    var userName = ""
    var avatar = ""

    private val mFragments = arrayListOf<Fragment>()
    val CAMERA_CODE = 200
    lateinit var popupWindow: PopupWindow
    lateinit var userInfoBean: UserInfoBean


    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<ChatDetailViewModel>()

    override fun setStatusBar() {
        super.setStatusBar()
//        StatusBarUtil.setColor(this, com.alan.mvvm.common.R.color.white.getResColor(), 0)
    }

    /**
     * 初始化View
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun ActivityChatBinding.initView() {
        initListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        mBinding.ivBack.clickDelay { finish() }
        mBinding.ivMore.clickDelay { showPopupWindow() }
        mBinding.tvInvite.clickDelay {
            when (mBinding.tvInvite.text) {
                "立即聊天" -> {
                    mViewModel.requestChatStart(userId)
                }
                "立即投喂" -> {

                }
                "立即关注" -> {
                    mViewModel.requestChangeFollow(userId, 1)
                }
            }
        }

        mBinding.ivAvatarTop.clickDelay {
            val bundle = Bundle().apply {
                putString("userId", userId)
            }
            jumpARoute(RouteUrl.MyModule.ACTIVITY_MY_MANAGER, bundle)
        }

        mBinding.tvOpen.clickDelay {
            JPushInterface.goToAppNotificationSettings(this@ChatActivity)
        }

        mBinding.ivCamera.clickDelay {
            PermissionX.init(this).permissions(REQUESTED_PERMISSIONS)
                .request { allGranted, grantedList, deniedList ->
                    //不给权限可以进
                    if (allGranted) {
                        jumpARoute(
                            RouteUrl.ChatModule.ACTIVITY_CHAT_CAMERA,
                            this@ChatActivity,
                            CAMERA_CODE
                        )
                    }
                }
        }
        mBinding.ivVoice.clickDelay {
            PermissionX.init(this).permissions(REQUESTED_PERMISSIONS[0], REQUESTED_PERMISSIONS[1])
                .request { allGranted, grantedList, deniedList ->
                    //不给权限可以进
                    if (allGranted) {
                        if (mBinding.llPress.isVisible) {
                            mBinding.ivVoice.setImageResource(R.drawable.icon_chat_voice)
                            mBinding.llPress.gone()
                            if (!TextUtils.isEmpty(mBinding.etMsg.text.toString())) {
                                mBinding.tvSend.visible()
                            } else {
                                mBinding.tvSend.gone()
                            }
                        } else {
                            mBinding.ivVoice.setImageResource(R.drawable.icon_chat_input)
                            mBinding.llPress.visible()
                            mBinding.tvSend.gone()
                        }
                    }
                }
        }
        mBinding.ivCall.clickDelay {
            PermissionX.init(this).permissions(REQUESTED_PERMISSIONS[0], REQUESTED_PERMISSIONS[1])
                .request { allGranted, grantedList, deniedList ->
                    //不给权限可以进
                    if (allGranted) {
                        mViewModel.requestChatStart(userId)
                    }
                }
        }
        mBinding.ivPic.clickDelay {
            ImageSelectUtil.singlePic(this@ChatActivity)
        }
        mBinding.ivGift.clickDelay {
            toast("即将上线")
        }
        mBinding.tvSend.clickDelay {
            val msg: String = mBinding.etMsg.getText().toString()
            if (TextUtils.isEmpty(msg)) {
                return@clickDelay
            }
            mBinding.etMsg.setText("")
            transVoice(msg)
        }
        mBinding.ivLeft.clickDelay {
            mBinding.ivLeft.gone()
            mBinding.ivRight.visible()
            mBinding.viewpager.setCurrentItem(0)
        }
        mBinding.ivRight.clickDelay {
            mBinding.ivLeft.visible()
            mBinding.ivRight.gone()
            mBinding.viewpager.setCurrentItem(1)
        }


        mBinding.llPress.setOnTouchListener { v, event ->
            mBinding.rlRecording.onPressToSpeakBtnTouch(
                v,
                event,
                object : VoiceRecorderView.EaseVoiceRecorderCallback {
                    override fun onVoiceRecordComplete(
                        voiceFilePath: String?,
                        voiceTimeLength: Int
                    ) {
                        sendVoiceMessage(voiceFilePath, voiceTimeLength)
                    }
                })
            true
        }

        mBinding.etMsg.addTextChangedListener {
            if (!TextUtils.isEmpty(it.toString())) {
                mBinding.tvSend.visible()
            } else {
                mBinding.tvSend.gone()
            }
        }
        mBinding.etMsg.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                checkVoice()
            }
        }
//        mBinding.etMsg.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEND ||
//                event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
//            ) {
//                val msg: String = mBinding.etMsg.getText().toString()
//                mBinding.etMsg.setText("")
//                sendTextMessage(msg)
//                true
//            } else {
//                false
//            }
//        })

        //高度监听
        val decorView = window.decorView
        decorView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            decorView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = decorView.rootView.height
            val heightDiff = screenHeight - rect.bottom
            val layoutParams = mBinding.clRoot.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(0, 0, 0, heightDiff)
            mBinding.clRoot.requestLayout()
        }

        initViewpager()
    }

    fun initViewpager() {
        if (mFragments.isNotEmpty()) {
            mFragments.clear()
        }
        mFragments.add(SpeakFragment.newInstance(userId))
        mFragments.add(ChatDetailFragment.newInstance(userId))
        mBinding.viewpager.offscreenPageLimit = 2
        mBinding.viewpager.adapter = ViewpagerAdapter(mFragments, supportFragmentManager, lifecycle)
        mBinding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        mBinding.ivLeft.gone()
                        mBinding.ivRight.visible()
                        getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    }
                    1 -> {
                        mBinding.ivLeft.visible()
                        mBinding.ivRight.gone()
                        getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                    }
                }
            }
        })
    }


    /**
     * 检查通知权限
     */
    fun checkNotifi() {
        //是否有通知权限
        val notificationEnabled = JPushInterface.isNotificationEnabled(this@ChatActivity)
        if (notificationEnabled != 1) {
            //没开启通知权限
            mBinding.clNotifi.visible()
        } else {
            mBinding.clNotifi.gone()
        }
    }

    /**
     * 显示菜单项
     */
    fun showPopupWindow() {
        val contentview: View = LayoutInflater.from(this).inflate(R.layout.layout_more, null)
        contentview.isFocusable = true
        contentview.isFocusableInTouchMode = true
        val tv_focus = contentview.findViewById<TextView>(R.id.tv_focus)
        val tvReport = contentview.findViewById<TextView>(R.id.tv_report)
        val view_line = contentview.findViewById<View>(R.id.view_line)

        if (userInfoBean.followStatus == 0) {
            tv_focus.setText("关注")
        } else if (userInfoBean.followStatus == 1) {
            tv_focus.setText("已关注")
        } else if (userInfoBean.followStatus == 2) {
            tv_focus.setText("互相关注")
        }


        tvReport.clickDelay {
            popupWindow.dismiss()
            val bundle = Bundle().apply {
                putString(
                    "webUrl",
                    HttpBaseUrlConstant.BASE_H5URL + "#/freedomspeak-report?reportFromUserid=${SpHelper.getUserInfo()!!.userId}&reportToUserid=${userId}"
                )
                putString("webTitle", "举报")
            }
            jumpARoute(RouteUrl.WebModule.ACTIVITY_WEB_WEB, bundle)
        }
        tv_focus.clickDelay {
            popupWindow.dismiss()
            if (userInfoBean.followStatus == 0) {
                mViewModel.requestChangeFollow(userId, 1)
            } else {
                mViewModel.requestChangeFollow(userId, 0)
            }
        }

        popupWindow = PopupWindow(
            contentview,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setFocusable(true)
        popupWindow.setOutsideTouchable(false)
        popupWindow.showAsDropDown(mBinding.ivMore, -dp2px(88f), 10)
    }


    /**
     * 订阅数据
     */
    override fun initObserve() {
        mViewModel.ldSuccess.observe(this) {
            when (it) {
                is UserInfoBean -> {
                    userInfoBean = it
                    mBinding.tvTitle.setText(userInfoBean.userName)
                    if (userInfoBean.onlineStatus!! && !TextUtils.isEmpty(userInfoBean.recentDoingTag)) {
                        mBinding.tvTime.setText("${userInfoBean.onlineStatusDesc}·${userInfoBean.recentDoingTag}")
                    } else {
                        mBinding.tvTime.setText("${userInfoBean.onlineStatusDesc}")
                    }
                    CoilUtils.loadCircle(mBinding.ivAvatarTop, userInfoBean.avatar)
                    CoilUtils.loadCircle(mBinding.ivAvatar, userInfoBean.avatar)
                    //加入数据库
                    userName = userInfoBean.userName
                    avatar = userInfoBean.avatar
                    EMClientHelper.saveUser(
                        UserEntity(
                            userId,
                            userInfoBean.userName,
                            userInfoBean.avatar
                        )
                    )
                    val fragment = mFragments[0] as SpeakFragment
                    fragment.updateUserInfo(userInfoBean)
                }
                is MatchStatusBean -> {
                    if (it.inMatch) {
                        mBinding.tvInfo.setText("${userName}${it.orderStatus},和Ta聊聊吧！")
                        mBinding.tvInvite.setText("立即聊天")
                    } else {
                        if (it.isFollowed) {
                            mBinding.tvInfo.setText("${userName}正在等待投喂，投喂Ta周饭卡可开启语音聊天，边吃边聊")
                            mBinding.tvInvite.setText("立即投喂")
                        } else {
                            mBinding.tvInfo.setText("关注${userName}，Ta干饭时会给你发送通知")
                            mBinding.tvInvite.setText("立即关注")
                        }
                    }
                }
                is CallBean -> {
                    val userInfoBean = SpHelper.getUserInfo()
                    val map = mutableMapOf<String, String>().apply {
                        put("userId", userInfoBean?.userId!!)
                        put("userName", userInfoBean.userName)
                        put("avatar", userInfoBean.avatar)
                    }
                    EMClientHelper.setUserInfoCallKit(userId, userName, avatar)
                    EMClientHelper.startSingleVoiceCall(userId, map)
                }

                is SpeakVoiceBean -> {
                    //语音
                    sendTextMessage(it.content, it.audio)
                }
            }
        }
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {
        mViewModel.requestUserInfo(userId)
        mViewModel.requestCheckMatch(userId)
        if (!TextUtils.isEmpty(content)) {
            transVoice(content)
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
                    if (selectList != null && !selectList.isEmpty()) {
                        val media = selectList.get(0)
                        val url = if (media.isCompressed) {
                            media.compressPath
                        } else {
                            media.getPath()
                        }
                        sendImageMessage(url)
                    }
                }
                CAMERA_CODE -> {
                    val type = data?.getIntExtra("type", Constants.TYPE_IMAGE)
                    when (type) {
                        Constants.TYPE_IMAGE -> {
                            val path = data.getStringExtra("bitmap")
                            sendImageMessage(path)

                        }
                        Constants.TYPE_VIDEO -> {
                            val path = data.getStringExtra("bitmap")
                            val url = data.getStringExtra("url")
                            var duration = 0
                            val player = MediaPlayer()
                            try {
                                player.setDataSource(url)
                                player.prepare()
                                duration = player.duration / 1000
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                            sendVideoMessage(url, path, if (duration == 0) 1 else duration)
                        }
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        checkNotifi()
    }


    /**
     * 检查是否上传声音文件
     */
    fun checkVoice() {
        //是否有通知权限
        val userInfo = SpHelper.getUserInfo()
        if (!userInfo?.setVoice!!) {
            DialogHelper.showMultipleDialog(this, "先去测测你的声音吧", "测一测", "我再想想", {
                mBinding.etMsg.clearFocus()
                jumpARoute(RouteUrl.MainModule.ACTIVITY_MAIN_SOUND)
            }, {
                finish()
            })
        }
    }


    fun transVoice(content: String) {
        mViewModel.requestVoiceTTS(content)
    }


    //==================================== 发送消息模块 start ======================================
    /**
     * 发送文本消息
     * @param content
     */
    protected fun sendTextMessage(content: String?, audio: String?) {
        val message = EMMessage.createTxtSendMessage(content, userId)
        sendMessage(message, audio)
    }


    /**
     * 发送语音消息
     * @param filePath
     * @param length
     */
    protected fun sendVoiceMessage(filePath: String?, length: Int) {
        val message = EMMessage.createVoiceSendMessage(filePath, length, userId)
        sendMessage(message)
    }

    /**
     * 发送图片
     * @param imagePath
     */
    protected fun sendImageMessage(imagePath: String?) {
        val message = EMMessage.createImageSendMessage(imagePath, false, userId)
        sendMessage(message)
    }

    /**
     * 发送图片
     * @param imagePath
     */
    protected fun sendImageMessage(imageUri: Uri?) {
        val message = EMMessage.createImageSendMessage(imageUri, false, userId)
        sendMessage(message)
    }

    /**
     * 发送视频消息
     * @param videoPath
     * @param thumbPath
     * @param videoLength
     */
    protected fun sendVideoMessage(videoPath: String?, thumbPath: String?, videoLength: Int) {
        val message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, userId)
        sendMessage(message)
    }

    /**
     * 发送视频消息
     * @param videoPath
     * @param thumbPath
     * @param videoLength
     */
    protected fun sendVideoMessage(videoUri: Uri?, thumbPath: String?, videoLength: Int) {
        val message = EMMessage.createVideoSendMessage(videoUri, thumbPath, videoLength, userId)
        sendMessage(message)
    }

    /**
     * 最终发送消息
     * @param message
     */
    protected fun sendMessage(message: EMMessage?, audio: String? = "") {
        if (message == null) {
            toast("请检查消息附件是否存在！")
            return
        }
        // 增加自己特定的属性
        message.setAttribute(IMConstant.MESSAGE_ATTR_AVATAR, SpHelper.getUserInfo()?.avatar);
        message.setAttribute(IMConstant.MESSAGE_ATTR_USERNAME, SpHelper.getUserInfo()?.userName);
        message.setAttribute(IMConstant.MESSAGE_ATTR_VOICE, audio);

        // 设置自定义扩展字段-强制推送
//        message.setAttribute(IMConstant.MESSAGE_ATTR_FORCEPUSH, true);
        // 设置自定义扩展字段-发送静默消息（不推送）
//        message.setAttribute(IMConstant.MESSAGE_ATTR_IGNOREPUSH, true);

        EMClientHelper.chatManager.sendMessage(message)

        val fragment = mFragments[1] as ChatDetailFragment
        fragment.refreshToLatest()
        val speakFragment = mFragments[0] as SpeakFragment
        speakFragment.playTextToVoice(EMMsgEvent(message))
    }


//============================== 发送消息模块 end ==============================================


}