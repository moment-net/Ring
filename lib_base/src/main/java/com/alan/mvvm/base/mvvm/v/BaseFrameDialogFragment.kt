package com.alan.mvvm.base.mvvm.v

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.alan.mvvm.base.R
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.BindingReflex
import com.alan.mvvm.base.utils.EventBusRegister
import com.alan.mvvm.base.utils.EventBusUtils
import com.alan.mvvm.base.utils.ViewRecreateHelper
import com.alibaba.android.arouter.launcher.ARouter

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：DialogFragment基类
 */
abstract class BaseFrameDialogFragment<VB : ViewBinding, VM : BaseViewModel> : DialogFragment(),
    FrameView<VB> {

    protected val mBinding: VB by lazy(mode = LazyThreadSafetyMode.NONE) {
        BindingReflex.reflexViewBinding(javaClass, layoutInflater)
    }
    protected val TAG = "base_dialog"
    protected abstract val mViewModel: VM
    protected lateinit var mActivity: FragmentActivity
    protected var mDimAmount = 0.5f
    protected var mClickBackIsAble = true
    protected var mCanceledOnTouchOutside = true


    /**
     * fragment状态保存工具类
     */
    private var mStatusHelper: FragmentStatusHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = requireActivity()
        setStyle(STYLE_NO_TITLE, getStyleId())
        initParam()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(getClickBackIsAble())
        dialog!!.setCanceledOnTouchOutside(getCanceledOnTouchOutside())
        if (!getClickBackIsAble()) {
            dialog!!.setOnKeyListener(mBackKeyListener)
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //处理恢复
        mStatusHelper?.onRestoreInstanceStatus(savedInstanceState)
        // ARouter 依赖注入
        ARouter.getInstance().inject(this)
        // 注册EventBus
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.register(this)

        mBinding.initView()
        initObserve()
        initRequestData()
    }

    override fun isRecreate(): Boolean = mStatusHelper?.isRecreate ?: false

    /**
     * 页面可能重建的时候回执行此方法，进行当前页面状态保存
     */
    override fun onSaveInstanceState(outState: Bundle) {
        if (mStatusHelper == null) {
            //仅当触发重建需要保存状态时创建对象
            mStatusHelper = FragmentStatusHelper(outState)
        } else {
            mStatusHelper?.onSaveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }

    /**
     * - fragment状态保存帮助类；
     * - 暂时没有其他需要保存的--空继承
     */
    private class FragmentStatusHelper(savedInstanceState: Bundle? = null) :
        ViewRecreateHelper(savedInstanceState)

    override fun onStart() {
        super.onStart()
        initWindow()
    }

    open fun getFragmentTag(): String? {
        return TAG
    }

    open fun show(fragmentManager: FragmentManager, bundle: Bundle?) {
        if (bundle != null) {
            arguments = bundle
        }
        fragmentManager
            .beginTransaction()
            .add(this, getFragmentTag())
            .commitNowAllowingStateLoss()
    }

    open fun show(fragmentManager: FragmentManager) {
        fragmentManager
            .beginTransaction()
            .add(this, getFragmentTag())
            .commitNowAllowingStateLoss()
    }

    override fun dismiss() {
        dismissAllowingStateLoss()
    }


    override fun onDestroy() {
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.unRegister(
            this
        )
        super.onDestroy()
    }

    open fun getStyleId(): Int {
        return R.style.BaseFragmentDialog
    }

    open fun initParam() {}

    //监听这个事件屏蔽掉返回键
    protected var mBackKeyListener =
        DialogInterface.OnKeyListener { dialog: DialogInterface?, keyCode: Int, event: KeyEvent? ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return@OnKeyListener true
            }
            false
        }

    protected open fun initWindow() {
        val window = dialog!!.window
        val params = window!!.attributes
        params.dimAmount = getDimAmount()
        if (getWidth() > 0) {
            params.width = getWidth()
        } else {
            params.width = WindowManager.LayoutParams.MATCH_PARENT
        }
        if (getHeight() > 0) {
            params.height = getHeight()
        } else {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        params.gravity = checkGravity()
        window.attributes = params
    }

    protected open fun getWidth(): Int {
        return -1
    }

    protected open fun getHeight(): Int {
        return -1
    }

    /**
     * 需要在 show之前使用
     *
     * @param canceledOnTouchOutside 点击外部区域是否可以取消
     * @return
     */
    open fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean): BaseFrameDialogFragment<VB, VM> {
        mCanceledOnTouchOutside = canceledOnTouchOutside
        return this
    }

    /**
     * 点击外部区域是否可以返回
     *
     * @return
     */
    open fun getCanceledOnTouchOutside(): Boolean {
        return mCanceledOnTouchOutside
    }

    /**
     * 点击物理返回键时候是否可以返回
     *
     * @return
     */
    open fun getClickBackIsAble(): Boolean {
        return mClickBackIsAble
    }

    /**
     * 点击物理返回键时候是否可以返回
     *
     * @param mClickBackIsAble
     * @return
     */
    open fun setClickBackIsAble(mClickBackIsAble: Boolean): BaseFrameDialogFragment<VB, VM> {
        this.mClickBackIsAble = mClickBackIsAble
        return this
    }

    /**
     * 子类继承复写可以修改布局位置
     *
     * @return int
     */
    open fun checkGravity(): Int {
        return Gravity.CENTER
    }

    open fun setDimAmount(dimAmount: Float): BaseFrameDialogFragment<VB, VM> {
        mDimAmount = dimAmount
        return this
    }

    /**
     * 背景透明度
     *
     * @return
     */
    open fun getDimAmount(): Float {
        return mDimAmount
    }
}