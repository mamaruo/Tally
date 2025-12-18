package com.mamaruo.tally.data.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * 交易与分类的联合查询结果
 */
data class TransactionWithCategory(
    @Embedded
    val transaction: Transaction,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category
)
