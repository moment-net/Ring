package com.alan.module.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alan.mvvm.base.http.callback.RequestCallback
import com.alan.mvvm.base.http.requestbean.BanRequestBean
import com.alan.mvvm.base.mvvm.vm.BaseViewModel
import com.alan.mvvm.base.utils.RequestUtil
import com.alan.mvvm.base.utils.toast
import com.alan.mvvm.common.http.model.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 作者：alan
 * 时间：2021/7/28
 * 备注：首页的VM层
 * @property mRepository CommonRepository 仓库层 通过Hilt注入
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val mRepository: CommonRepository) :
    BaseViewModel() {

    val ldData = MutableLiveData<Any>()
    val ldCard = MutableLiveData<Any>()
    val ldBanner = MutableLiveData<Any>()



    /**
     * 查看用户干饭状态
     */
    fun requestMealStatus() {
        viewModelScope.launch() {
            mRepository.requestMealStatus(
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = it
                    },
                    onFailed = {
                        ldData.value = it
                    },
                )
            )
        }
    }

    /**
     * 获取匹配用户
     */
    fun requestNowMatch() {
        viewModelScope.launch() {
            mRepository.requestNowMatch(
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = it.data!!
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }

    /**
     * 列表
     */
    fun requestCardAllList() {
        viewModelScope.launch() {
            mRepository.requestCardAllList(
                "match",
                callback = RequestCallback(
                    onSuccess = {
                        ldCard.value = it
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }

    /**
     * 列表
     */
    fun requestBanner() {
        viewModelScope.launch() {
            mRepository.requestBanner(
                callback = RequestCallback(
                    onSuccess = {
                        ldBanner.value = it
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }

    /**
     * 匹配信息
     */
    fun requestMatchInfo() {
        viewModelScope.launch() {
            mRepository.requestMatchInfo(
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = it.data!!
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }

    /**
     * 开始匹配
     */
    fun requestMatchJoin() {
        viewModelScope.launch() {
            mRepository.requestMatchJoin(
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = 1
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }

    /**
     * 结束匹配
     */
    fun requestMatchStop() {
        viewModelScope.launch() {
            mRepository.requestMatchStop(
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = 2
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }


    /**
     * 获取首页列表
     */
    fun requestList(curson: Int) {
        val map = hashMapOf<String, String>()
        map.put("cursor", "${curson}")
        map.put("count", "20")
        viewModelScope.launch() {
            mRepository.requestThinkList(
                map,
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = it
                    },
                    onFailed = {
                        ldData.value = it
                    },
                )
            )
        }
    }

    /**
     * 屏蔽
     */
    fun requestBanThink(id: String, position: Int) {
        val banRequestBean = BanRequestBean(id, "-1")
        viewModelScope.launch() {
            mRepository.requestBanThink(
                RequestUtil.getPostBody(banRequestBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = position
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }

    /**
     * 赞、取消
     */
    fun requestZan(id: String, action: Int, position: Int) {
        val banRequestBean = BanRequestBean(id, "$action")
        viewModelScope.launch() {
            mRepository.requestZan(
                RequestUtil.getPostBody(banRequestBean),
                callback = RequestCallback(
                    onSuccess = {
                        ldData.value = Pair<Int, Int>(action, position)
                    },
                    onFailed = {
                        toast(it.errorMessage)
                    },
                )
            )
        }
    }
}