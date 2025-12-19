package com.mamaruo.tally.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mamaruo.tally.data.model.TransactionWithCategory
import com.mamaruo.tally.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

/**
 * 主页 UI 状态
 */
data class HomeUiState(
    val monthlyExpense: Long = 0,      // 当月支出（分）
    val monthlyIncome: Long = 0,       // 当月收入（分）
    val transactions: List<TransactionWithCategory> = emptyList(),
    val currentMonth: LocalDate = LocalDate.now()
) {
    /** 当月结余 = 收入 - 支出 */
    val monthlyBalance: Long get() = monthlyIncome - monthlyExpense

    /** 按日期分组的交易 */
    val transactionsByDate: Map<LocalDate, List<TransactionWithCategory>>
        get() = transactions.groupBy { it.transaction.date }
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        transactionRepository.getCurrentMonthExpense(),
        transactionRepository.getCurrentMonthIncome(),
        transactionRepository.getCurrentMonthTransactions()
    ) { expense, income, transactions ->
        HomeUiState(
            monthlyExpense = expense,
            monthlyIncome = income,
            transactions = transactions
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )
}
