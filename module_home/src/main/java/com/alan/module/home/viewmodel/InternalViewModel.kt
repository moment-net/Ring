package com.alan.module.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.module.home.repository.InternalRepository
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class InternalViewModel @Inject constructor(private val repository: InternalRepository) :
    BaseViewModel() {

    /**
     * 重建计数
     */
    val recreatedCont = MutableLiveData<Int>()

    /**
     * 首个数据
     */
    val firstData = MutableLiveData<String>()

    /**
     * 累加重建次数
     */
    fun increase() {
        val value = recreatedCont.value ?: 0
        recreatedCont.value = value + 1
    }

    /**
     * 获取数据
     */
    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getData()
                .catch {
                    Log.d("DJC", "getData: ")
                }
                .onStart { changeStateView(loading = true) }
                .collect {
                    changeStateView(hide = true)
                    delay(200)
                    firstData.postValue(it)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("DJC", "InternalViewModel Clear")
    }
}
