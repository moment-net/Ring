package com.alan.mvvm.base.mvvm.vm

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：空的ViewModel 主要给现阶段不需要ViewModel的界面使用
 */
@HiltViewModel
class EmptyViewModel @Inject constructor() : BaseViewModel()