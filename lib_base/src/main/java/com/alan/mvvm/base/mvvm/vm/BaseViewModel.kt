package com.alan.mvvm.base.mvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alan.mvvm.base.utils.StateLayoutEnum

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：ViewModel 基类
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * 控制状态视图的LiveData
     */
    val ld_state = MutableLiveData<StateLayoutEnum>()

    /**
     * 更改状态视图的状态
     *
     * @param loading Boolean 是否显示加载中视图
     * @param hide Boolean 是否隐藏加载中视图
     * @param error Boolean 是否显示错误视图
     * @param noData Boolean 是否显示没有数据视图
     * @return Unit
     * @throws IllegalArgumentException 如果入参没有传入任何参数或者为true的参数 >1 时，会抛出[IllegalArgumentException]
     */
    @Throws(IllegalArgumentException::class)
    protected fun changeStateView(
        loading: Boolean = false,
        hide: Boolean = false,
        error: Boolean = false,
        noData: Boolean = false
    ) {
        // 对参数进行校验
        var count = 0
        if (loading) count++
        if (hide) count++
        if (error) count++
        if (noData) count++
        when {
            count == 0 -> throw IllegalArgumentException("必须设置一个参数为true")
            count > 1 -> throw IllegalArgumentException("只能有一个参数为true")
        }

        // 修改状态
        when {
            loading -> ld_state.postValue(StateLayoutEnum.LOADING)
            hide -> ld_state.postValue(StateLayoutEnum.HIDE)
            error -> ld_state.postValue(StateLayoutEnum.ERROR)
            noData -> ld_state.postValue(StateLayoutEnum.NO_DATA)
        }
    }
}