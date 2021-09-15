package com.alan.mvvm.common.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * (1) @Entity注解中我们传入了一个参数 tableName用来指定表的名称,如果不传默认类名为表名
 *（2）@PrimaryKey注解用来标注表的主键，并且使用autoGenerate = true 来指定了主键自增长
 *（3）@ColumnInfo注解用来标注表对应的列的信息比如表名、默认值等等
 *（4）@Ignore 注解顾名思义就是忽略这个字段，使用了这个注解的字段将不会在数据库中生成对应的列信息。也可以使用@Entity注解中的 ignoredColumns 参数来指定，效果是一样的
 */
@Entity(tableName = "users")
class UserEntity(
    @PrimaryKey(autoGenerate = false) var userId: String,
    @ColumnInfo(name = "user_name") var userName: String,
    @ColumnInfo(name = "avatar") var avatar: String
)