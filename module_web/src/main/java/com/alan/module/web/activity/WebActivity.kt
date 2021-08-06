package com.alan.module.web.activity


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.KeyEvent
import androidx.activity.viewModels
import com.alan.module.web.databinding.ActivityWebBinding
import com.alan.mvvm.base.ktx.clickDelay
import com.alan.mvvm.base.mvvm.vm.EmptyViewModel
import com.alan.mvvm.common.constant.RouteUrl
import com.alan.mvvm.common.ui.BaseActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.socks.library.KLog
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import dagger.hilt.android.AndroidEntryPoint

/**
 * 作者：alan
 * 时间：2021/7/30
 * 备注：
 */
@Route(path = RouteUrl.WebModule.ACTIVITY_WEB_WEB)
@AndroidEntryPoint
class WebActivity : BaseActivity<ActivityWebBinding, EmptyViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<EmptyViewModel>()

    @Autowired
    lateinit var webUrl: String

    @Autowired
    lateinit var webTitle: String


    /**
     * 初始化View
     */
    override fun ActivityWebBinding.initView() {
        ivBack.clickDelay { finish() }
        initWeb()
    }

    fun initWeb() {
        var webSetting: WebSettings = mBinding.webview.getSettings()
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        //webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setPluginState(WebSettings.PluginState.ON);//视频播放
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.LOAD_NORMAL);
        }
        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计


        mBinding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                KLog.e("xujm", "xs sdk = " + Build.VERSION.SDK_INT);//23
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url);
                } else {
                    if (view.getContext() != null) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        view.getContext().startActivity(intent);
                    }
                }
                return true
            }
        }


    }

    /**
     * 订阅数据
     */
    override fun initObserve() {
        mBinding.tvTitle.setText(webTitle)
        mBinding.webview.loadUrl(webUrl)
    }

    /**
     * 获取数据
     */
    override fun initRequestData() {

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBinding.webview.canGoBack()) {
            mBinding.webview.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}