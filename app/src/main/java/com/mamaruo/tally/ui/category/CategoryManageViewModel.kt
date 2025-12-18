package com.mamaruo.tally.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mamaruo.tally.data.model.Category
import com.mamaruo.tally.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * 分类管理 UI 状态
 */
data class CategoryManageUiState(
    val categories: List<Category> = emptyList()
)

@HiltViewModel
class CategoryManageViewModel @Inject constructor(
    categoryRepository: CategoryRepository
) : ViewModel() {
    
    val uiState: StateFlow<CategoryManageUiState> = categoryRepository
        .getAllCategories()
        .map { CategoryManageUiState(categories = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CategoryManageUiState()
        )
}
