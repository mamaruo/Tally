package com.mamaruo.tally.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * 交易实体
 */
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("categoryId")]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /** 交易类型：收入或支出 */
    val type: TransactionType,
    /** 金额（以分为单位存储，避免浮点误差） */
    val amountMinor: Long,
    /** 交易日期 */
    val date: LocalDate,
    /** 分类ID */
    val categoryId: Long,
    /** 备注（可选） */
    val note: String? = null,
    /** 创建时间戳（用于同一天内排序） */
    val createdAt: Long = System.currentTimeMillis()
)
