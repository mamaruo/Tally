package com.mamaruo.tally.data.local

import androidx.room.TypeConverter
import com.mamaruo.tally.data.model.TransactionType
import java.time.LocalDate

/**
 * Room 类型转换器
 */
class Converters {
    
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString() // ISO-8601 格式: "yyyy-MM-dd"
    }
    
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }
    }
    
    @TypeConverter
    fun fromTransactionType(type: TransactionType): String {
        return type.name
    }
    
    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }
}
