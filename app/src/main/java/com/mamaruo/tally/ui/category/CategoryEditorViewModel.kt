package com.mamaruo.tally.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mamaruo.tally.data.model.Category
import com.mamaruo.tally.data.model.TransactionType
import com.mamaruo.tally.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 分类编辑 UI 状态
 */
data class CategoryEditorUiState(
    val name: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val selectedIconKey: String = "Restaurant",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
) {
    val isValid: Boolean
        get() = name.isNotBlank() && selectedIconKey.isNotBlank()
}

@HiltViewModel
class CategoryEditorViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CategoryEditorUiState())
    val uiState: StateFlow<CategoryEditorUiState> = _uiState.asStateFlow()
    
    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }
    
    fun onTypeChanged(type: TransactionType) {
        _uiState.update { it.copy(type = type) }
    }
    
    fun onIconSelected(iconKey: String) {
        _uiState.update { it.copy(selectedIconKey = iconKey) }
    }
    
    fun save() {
        val state = _uiState.value
        
        if (state.name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "请输入分类名称") }
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val category = Category(
                    name = state.name.trim(),
                    type = state.type,
                    iconKey = state.selectedIconKey,
                    isDefault = false
                )
                categoryRepository.insertCategory(category)
                _uiState.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "保存失败: ${e.message}") }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
