package com.mamaruo.tally.data.repository

import com.mamaruo.tally.data.local.CategoryDao
import com.mamaruo.tally.data.model.Category
import com.mamaruo.tally.data.model.TransactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    
    fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }
    
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(type)
    }
    
    suspend fun getCategoryById(id: Long): Category? {
        return categoryDao.getCategoryById(id)
    }
    
    suspend fun insertCategory(category: Category): Long {
        return categoryDao.insertCategory(category)
    }
}
