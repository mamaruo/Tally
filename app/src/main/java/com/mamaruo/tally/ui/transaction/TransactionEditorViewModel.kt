package com.mamaruo.tally.ui.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mamaruo.tally.data.model.Category
import com.mamaruo.tally.data.model.Transaction
import com.mamaruo.tally.data.model.TransactionType
import com.mamaruo.tally.data.repository.CategoryRepository
import com.mamaruo.tally.data.repository.TransactionRepository
import com.mamaruo.tally.ui.util.AmountFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * 交易编辑 UI 状态
 */
data class TransactionEditorUiState(
    val isEditMode: Boolean = false,
    val transactionId: Long? = null,
    val type: TransactionType = TransactionType.EXPENSE,
    val amountText: String = "",
    val date: LocalDate = LocalDate.now(),
    val selectedCategoryId: Long? = null,
    val note: String = "",
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
    val errorMessage: String? = null
) {
    val isValid: Boolean
        get() = amountText.isNotBlank() && 
                AmountFormatter.parseToMinor(amountText) != null &&
                AmountFormatter.parseToMinor(amountText)!! > 0 &&
                selectedCategoryId != null
}

@HiltViewModel
class TransactionEditorViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val transactionId: Long? = savedStateHandle.get<Long>("transactionId")
        ?.takeIf { it != -1L }
    
    private val _uiState = MutableStateFlow(
        TransactionEditorUiState(
            isEditMode = transactionId != null,
            transactionId = transactionId
        )
    )
    val uiState: StateFlow<TransactionEditorUiState> = _uiState.asStateFlow()
    
    init {
        loadCategories()
        if (transactionId != null) {
            loadTransaction(transactionId)
        }
    }
    
    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getCategoriesByType(_uiState.value.type).collect { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
        }
    }
    
    private fun loadTransaction(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val transactionWithCategory = transactionRepository.getTransactionWithCategoryById(id)
            if (transactionWithCategory != null) {
                val transaction = transactionWithCategory.transaction
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        type = transaction.type,
                        amountText = AmountFormatter.toEditableString(transaction.amountMinor),
                        date = transaction.date,
                        selectedCategoryId = transaction.categoryId,
                        note = transaction.note ?: ""
                    )
                }
                // 重新加载对应类型的分类
                refreshCategories()
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "交易不存在") }
            }
        }
    }
    
    fun onTypeChanged(type: TransactionType) {
        if (type != _uiState.value.type) {
            _uiState.update { 
                it.copy(
                    type = type, 
                    selectedCategoryId = null,
                    categories = emptyList()
                ) 
            }
            refreshCategories()
        }
    }
    
    private fun refreshCategories() {
        viewModelScope.launch {
            categoryRepository.getCategoriesByType(_uiState.value.type).collect { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
        }
    }
    
    fun onAmountChanged(amount: String) {
        // 只允许输入数字和小数点，最多两位小数
        val filtered = amount.filter { it.isDigit() || it == '.' }
        val parts = filtered.split(".")
        val result = when {
            parts.size > 2 -> _uiState.value.amountText
            parts.size == 2 && parts[1].length > 2 -> parts[0] + "." + parts[1].take(2)
            else -> filtered
        }
        _uiState.update { it.copy(amountText = result) }
    }
    
    fun onDateChanged(date: LocalDate) {
        _uiState.update { it.copy(date = date) }
    }
    
    fun onCategorySelected(categoryId: Long) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }
    
    fun onNoteChanged(note: String) {
        _uiState.update { it.copy(note = note) }
    }
    
    fun save() {
        val state = _uiState.value
        val amountMinor = AmountFormatter.parseToMinor(state.amountText)
        
        if (amountMinor == null || amountMinor <= 0) {
            _uiState.update { it.copy(errorMessage = "请输入有效金额") }
            return
        }
        
        if (state.selectedCategoryId == null) {
            _uiState.update { it.copy(errorMessage = "请选择分类") }
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val transaction = Transaction(
                    id = state.transactionId ?: 0,
                    type = state.type,
                    amountMinor = amountMinor,
                    date = state.date,
                    categoryId = state.selectedCategoryId,
                    note = state.note.takeIf { it.isNotBlank() }
                )
                
                if (state.isEditMode && state.transactionId != null) {
                    transactionRepository.updateTransaction(transaction)
                } else {
                    transactionRepository.insertTransaction(transaction)
                }
                
                _uiState.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "保存失败: ${e.message}") }
            }
        }
    }
    
    fun delete() {
        val transactionId = _uiState.value.transactionId ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                transactionRepository.deleteTransactionById(transactionId)
                _uiState.update { it.copy(isLoading = false, isDeleted = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "删除失败: ${e.message}") }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
