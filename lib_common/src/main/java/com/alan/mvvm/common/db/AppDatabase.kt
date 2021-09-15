package com.alan.mvvm.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alan.mvvm.common.db.dao.UserDAO
import com.alan.mvvm.common.db.entity.UserEntity

/**
 * (1) 使用entities来映射相关的实体类
 * (2) version来指明当前数据库的版本号
 * (3) 使用了单例模式来返回Database，以防止新建过多的实例造成内存的浪费
 *（4）Room.databaseBuilder(context,klass,name).build()来创建Database，
 *    其中第一个参数为上下文，
 *    第二个参数为当前Database的class字节码文件，
 *    第三个参数为数据库名称
 * 注意事项：默认情况下Database是不可以在主线程中进行调用的。
 *          因为大部分情况，操作数据库都还算是比较耗时的动作。
 *          如果需要在主线程调用则使用allowMainThreadQueries进行说明。
 */
@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO

}