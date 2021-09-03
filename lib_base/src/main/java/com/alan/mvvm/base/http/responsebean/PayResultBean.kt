package com.alan.mvvm.base.http.responsebean

import android.text.TextUtils

class PayResultBean(rawResult: Map<String, String>) {

    var resultStatus: String? = null
    var result: String? = null
    var memo: String? = null


    init {
        for (key in rawResult.keys) {
            if (TextUtils.equals(key, "resultStatus")) {
                resultStatus = rawResult.get(key)
            } else if (TextUtils.equals(key, "result")) {
                result = rawResult.get(key)
            } else if (TextUtils.equals(key, "memo")) {
                memo = rawResult.get(key)
            }
        }
    }


}
