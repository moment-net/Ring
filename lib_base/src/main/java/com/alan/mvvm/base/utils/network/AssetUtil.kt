package com.alan.mvvm.base.utils.network

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object AssetUtil {

    fun toJson(context: Context, fileName: String): String {
        var stringBuild: StringBuilder = StringBuilder();
        try {
            val bf = BufferedReader(InputStreamReader(context.assets.open(fileName)))
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuild.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuild.toString()
    }


}