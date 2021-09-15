package com.alan.mvvm.common.db.dao

import androidx.room.*
import com.alan.mvvm.common.db.entity.UserEntity

@Dao
interface UserDAO {

    @Query("select * from users where userId = :id")
    fun getUserById(id: Long): UserEntity

    @Query("select * from users")
    fun getAllUsers(): List<UserEntity>

    // 参数onConflict，表示的是当插入的数据已经存在时候的处理逻辑，有三种操作逻辑：REPLACE、ABORT和IGNORE。
    // 如果不指定则默认为ABORT终止插入数据。这里我们将其指定为REPLACE替换原有数据。
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: UserEntity)

    @Delete
    fun deleteUserByUser(user: UserEntity)

    @Update
    fun updateUserByUser(user: UserEntity)

    // 上面说的 @Query 查询接受的参数是一个字符串，所以像删除或者更新我们也可以使用
    // @Query 注解来使用SQL语句来直接执行。比如根据userid来查询某个用户或者根据userid更新某个用户的姓名：

    @Query("delete from users where userId = :id ")
    fun deleteUserById(id: Long)

    @Query("update  users set user_name = :updateName where userID =  :id")
    fun update(id: Long, updateName: String)


}