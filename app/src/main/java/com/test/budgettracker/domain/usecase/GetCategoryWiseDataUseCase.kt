package com.test.budgettracker.domain.usecase

import com.test.budgettracker.data.model.CategoryExpense
import com.test.budgettracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetCategoryWiseDataUseCase(private val expenseRepository: ExpenseRepository)  {
    suspend fun execute(timeStart: Long, timeEnd: Long): Flow<List<CategoryExpense>> = expenseRepository.getCategoryWiseData(timeStart, timeEnd)
}