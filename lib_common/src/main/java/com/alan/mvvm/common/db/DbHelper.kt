package com.alan.mvvm.common.db

import android.content.Context
import android.text.TextUtils
import androidx.room.Room
import com.alan.mvvm.common.db.dao.UserDAO

class DbHelper private constructor() {
    var mDatabase: AppDatabase? = null
    var currentUser: String? = null

    companion object {
        val instance: DbHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DbHelper()
        }
    }

    /**
     * 初始化数据库
     */
    fun initDB(context: Context, user: String) {
        if (currentUser != null) {
            if (TextUtils.equals(currentUser, user)) {
                return
            }
            closeDb()
        }
        currentUser = user
        val dbName = "ring${currentUser}.db"
        mDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            dbName
        ).allowMainThreadQueries().build()
    }

    /**
     * 关闭数据库
     */
    fun closeDb() {
        if (mDatabase != null) {
            mDatabase!!.close()
            mDatabase = null
        }
    }

    /**
     * 获取用户访问对象
     */
    fun getUserDao(): UserDAO? {
        if (mDatabase != null) {
            return mDatabase!!.userDao()
        }
        return null
    }


}