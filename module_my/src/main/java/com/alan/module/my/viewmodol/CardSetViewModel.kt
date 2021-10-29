package com.alan.module.my.viewmodol

import androidx.lifecycle.MutableLiveData
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.common.http.model.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：首页的VM层
 * @property mRepository CommonRepository 仓库层 通过Hilt注入
 */
@HiltViewModel
class CardSetViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val data = MutableLiveData<String>()


}