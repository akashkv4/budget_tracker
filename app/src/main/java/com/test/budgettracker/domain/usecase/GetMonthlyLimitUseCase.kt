package com.test.budgettracker.domain.usecase

 import com.test.budgettracker.data.model.Category
 import com.test.budgettracker.data.model.MonthlyLimit
 import com.test.budgettracker.domain.repository.ExpenseRepository
 import kotlinx.coroutines.flow.Flow

class GetMonthlyLimitUseCase(private val expenseRepository: ExpenseRepository)  {
    suspend fun execute(): Flow<List<MonthlyLimit>> = expenseRepository.getMonthlyLimit()

}