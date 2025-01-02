package com.test.budgettracker.domain.usecase

 import com.test.budgettracker.data.model.MonthlyLimit
 import com.test.budgettracker.domain.repository.ExpenseRepository

class UpdateMonthlyLimitUseCase(private val expenseRepository: ExpenseRepository)  {
    suspend fun execute(monthlyLimit: MonthlyLimit) = expenseRepository.updateMonthlyLimit(monthlyLimit)

}