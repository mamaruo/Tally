package com.mamaruo.tally.data.repository

import com.mamaruo.tally.data.local.TransactionDao
import com.mamaruo.tally.data.model.Transaction
import com.mamaruo.tally.data.model.TransactionType
import com.mamaruo.tally.data.model.TransactionWithCategory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {

    private val monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    /**
     * 获取指定月份的所有交易（带分类信息）
     */
    fun getTransactionsForMonth(date: LocalDate): Flow<List<TransactionWithCategory>> {
        val monthPrefix = date.format(monthFormatter)
        return transactionDao.getTransactionsForMonth(monthPrefix)
    }

    /**
     * 获取当月所有交易
     */
    fun getCurrentMonthTransactions(): Flow<List<TransactionWithCategory>> {
        return getTransactionsForMonth(LocalDate.now())
    }

    /**
     * 获取指定月份某类型的总金额
     */
    fun getMonthlyTotalByType(date: LocalDate, type: TransactionType): Flow<Long> {
        val monthPrefix = date.format(monthFormatter)
        return transactionDao.getMonthlyTotalByType(monthPrefix, type)
    }

    /**
     * 获取当月支出总额
     */
    fun getCurrentMonthExpense(): Flow<Long> {
        return getMonthlyTotalByType(LocalDate.now(), TransactionType.EXPENSE)
    }

    /**
     * 获取当月收入总额
     */
    fun getCurrentMonthIncome(): Flow<Long> {
        return getMonthlyTotalByType(LocalDate.now(), TransactionType.INCOME)
    }

    suspend fun getTransactionById(id: Long): Transaction? {
        return transactionDao.getTransactionById(id)
    }

    suspend fun getTransactionWithCategoryById(id: Long): TransactionWithCategory? {
        return transactionDao.getTransactionWithCategoryById(id)
    }

    suspend fun insertTransaction(transaction: Transaction): Long {
        return transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun deleteTransactionById(id: Long) {
        transactionDao.deleteTransactionById(id)
    }
}
