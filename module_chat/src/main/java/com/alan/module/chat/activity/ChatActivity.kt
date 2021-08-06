package com.alan.module.chat.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.viewModels
import com.alan.module.chat.R
import com.alan.module.chat.databinding.ActivityChatBinding
import com.alan.module.chat.viewmodol.ChatDetailViewModel
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.ChatModule.ACTIVITY_CHAT_DETAIL)
@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding, ChatDetailViewModel>() {

    private lateinit var popupWindow: PopupWindow

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<ChatDetailViewModel>()

    /**
     * 初始化View
     */
    override fun ActivityChatBinding.initView() {
        ivMore.clickDelay { showPopupWindow() }

    }

    fun showPopupWindow() {
        val contentview: View = LayoutInflater.from(this).inflate(R.layout.layout_more, null)
        contentview.isFocusable = true
        contentview.isFocusableInTouchMode = true
        val tv_focus = contentview.findViewById<TextView>(R.id.tv_focus)
        val tvReport = contentview.findViewById<TextView>(R.id.tv_report)
        val view_line = contentview.findViewById<View>(R.id.view_line)
        tvReport.clickDelay {
            popupWindow.dismiss()
            //跳转到浏览器，本地WebView不支持，需要自己实现
//            val roomId: String = if (viewModel.getChannelBean() != null) {
//                viewModel.getChannelBean().getId()
//            } else {
//                ""
//            }
//            val intent = Intent(
//                Intent.ACTION_VIEW,
//                Uri.parse(
//                    "scheme://speak:8888/webActivity?webUrl=" + ApiConstant.getHostUrl(
//                        Host.H5
//                    ).toString() + "&reportFromUserid=" + viewModel.getUserInfo().getUserId()
//                        .toString() + "&reportToUserid=" + userInfoBean.getUserId()
//                        .toString() + "&roomId=" + roomId + "&title=举报"
//                )
//            )
//            startActivity(intent)
        }
        tv_focus.clickDelay {
            popupWindow.dismiss()
//            val kickOutDialog: KickOutDialog = KickOutDialog.newInstance(userInfoBean)
//            kickOutDialog.show(fragmentManager)
        }
        if (true) {
            tv_focus.visibility = View.VISIBLE
            view_line.visibility = View.VISIBLE
        } else {
            tv_focus.visibility = View.GONE
            view_line.visibility = View.GONE
        }
        popupWindow = PopupWindow(
            contentview,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setFocusable(true)
        popupWindow.setOutsideTouchable(false)
        popupWindow.showAsDropDown(mBinding.ivMore, 0, 10)
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
}