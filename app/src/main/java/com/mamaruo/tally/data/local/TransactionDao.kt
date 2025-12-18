package com.mamaruo.tally.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mamaruo.tally.data.model.Transaction
import com.mamaruo.tally.data.model.TransactionType
import com.mamaruo.tally.data.model.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    
    /**
     * 获取指定月份的所有交易（带分类信息），按日期降序、创建时间降序排列
     */
    @androidx.room.Transaction
    @Query("""
        SELECT * FROM transactions 
        WHERE date LIKE :monthPrefix || '%'
        ORDER BY date DESC, createdAt DESC
    """)
    fun getTransactionsForMonth(monthPrefix: String): Flow<List<TransactionWithCategory>>
    
    /**
     * 获取指定月份某类型的总金额（分）
     */
    @Query("""
        SELECT COALESCE(SUM(amountMinor), 0) FROM transactions 
        WHERE date LIKE :monthPrefix || '%' AND type = :type
    """)
    fun getMonthlyTotalByType(monthPrefix: String, type: TransactionType): Flow<Long>
    
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): Transaction?
    
    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionWithCategoryById(id: Long): TransactionWithCategory?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long
    
    @Update
    suspend fun updateTransaction(transaction: Transaction)
    
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
    
    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)
}
