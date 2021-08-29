package com.alan.mvvm.base.utils

import android.content.Context
import android.view.View
import com.alan.mvvm.base.http.responsebean.ProvinceBean
import com.alan.mvvm.base.utils.network.AssetUtil
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*

object LocationPickerUtil {

    fun showPickerView(mContext: Context, listener: OnPickerListener) {
        CoroutineScope(Dispatchers.Main).launch {
            getProvince(mContext).collect {
                var list1: ArrayList<String> = arrayListOf();
                var list2: ArrayList<MutableList<String>> = arrayListOf()
                var list3: ArrayList<MutableList<MutableList<String>>> = arrayListOf()


                for (provinceBean in it) {
                    list1.add(provinceBean.name)

                    var cityList = arrayListOf<String>();
                    var areaList = arrayListOf<MutableList<String>>();

                    for (city in provinceBean.city) {
                        cityList.add(city.name)

                        areaList.add(city.area)
                    }


                    list2.add(cityList)
                    list3.add(areaList)
                }


                val pvOptions: OptionsPickerView<String> =
                    OptionsPickerBuilder(mContext) { options1: Int, options2: Int, options3: Int, v: View? ->
                        //返回的分别是三个级别的选中位置
                        val opt1tx = if (list1.size > 0) list1.get(options1) else ""
                        val opt2tx =
                            if (list2.size > 0 && list2.get(options1).size > 0) list2.get(options1)
                                .get(options2) else ""
                        val opt3tx =
                            if (list2.size > 0 && list3.get(options1).size > 0 && list3.get(options1)
                                    .get(options2).size > 0
                            ) list3.get(options1).get(options2).get(options3) else ""
                        if (listener != null) {
                            listener.onPicker(opt1tx, opt2tx, opt3tx)
                        }
                    }
                        .setTitleText("城市选择")
                        .build<String>()

                var option1 = 0
                var option2 = 0
                var option3 = 0
//                if (!TextUtils.isEmpty(location)) {
//                    val array: Array<String> = location.split("-".toRegex()).toTypedArray()
//                    if (array.size < 2) return
//                    for (i in list1.indices) {
//                        if (TextUtils.equals(array[0], list1.get(i).getName())) {
//                            option1 = i
//                            for (j in list2.indices) {
//                                if (TextUtils.equals(array[1], list2.get(i).get(j))) {
//                                    option2 = j
//                                    for (k in list3.indices) {
//                                        if (TextUtils.equals(
//                                                array[2],
//                                                list3.get(i).get(j).get(k)
//                                            )
//                                        ) {
//                                            option3 = k
//                                            break
//                                        }
//                                    }
//                                    break
//                                }
//                            }
//                            break
//                        }
//                    }
//                }
                pvOptions.setSelectOptions(option1, option2, option3)
                pvOptions.setPicker(list1, list2, list3) //三级选择器

                pvOptions.show()
            }
        }
    }


    /**
     * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
     * 关键逻辑在于循环体
     *
     */
    fun getProvince(mContext: Context): Flow<List<ProvinceBean>> {
        return flow<List<ProvinceBean>> {
            var json: String = AssetUtil.toJson(mContext, "province.json")
            var provinceList = GsonUtil.jsonToList(json, ProvinceBean::class.java)
            if (provinceList != null) {
                emit(provinceList)
            }
        }.flowOn(Dispatchers.IO)
    }

    interface OnPickerListener {
        fun onPicker(opt1: String, opt2: String, opt3: String)
    }

}