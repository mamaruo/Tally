package com.mamaruo.tally.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 分类实体
 */
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /** 分类名称 */
    val name: String,
    /** 分类类型：收入或支出 */
    val type: TransactionType,
    /** 图标键，用于映射到 Icons 的具体图标 */
    val iconKey: String,
    /** 是否为默认分类 */
    val isDefault: Boolean = false
)
